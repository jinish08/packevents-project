package com.jinish.ncsu.sad.bookingservice.service;

import com.jinish.ncsu.sad.bookingservice.dto.PaymentStatusEvent;
import com.jinish.ncsu.sad.bookingservice.model.Event;
import com.jinish.ncsu.sad.bookingservice.dto.BookingRequest;
import com.jinish.ncsu.sad.bookingservice.model.Booking;
import com.jinish.ncsu.sad.bookingservice.model.Seat;
import com.jinish.ncsu.sad.bookingservice.repository.BookingRepository;
import com.jinish.ncsu.sad.bookingservice.repository.EventRepository;
import com.jinish.ncsu.sad.bookingservice.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final KafkaTemplate<String, Booking> kafkaTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(BookingService.class);

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Seat> getAvailableSeats(String eventId) {
        return seatRepository.findByEventId(eventId);
    }

    @Transactional // Ensures all database operations succeed or fail together
    public Booking createBooking(BookingRequest bookingRequest) {
        // 1. Find all the requested seats
        List<Seat> seatsToBook = seatRepository.findAllById(bookingRequest.getSeatIds());

        // 2. Check if all seats are available
        for (Seat seat : seatsToBook) {
            if (!seat.isAvailable()) {
                throw new IllegalStateException("Seat " + seat.getSeatNumber() + " is not available.");
            }
        }

        // 3. Mark seats as unavailable and save them
        seatsToBook.forEach(seat -> seat.setAvailable(false));
        seatRepository.saveAll(seatsToBook);

        // 4. Create and save the booking record
        Booking booking = new Booking();
        booking.setUserId(bookingRequest.getUserId());
        booking.setEventId(bookingRequest.getEventId());
        booking.setSeatIds(bookingRequest.getSeatIds());
        booking.setStatus("PENDING");
        Booking savedBooking = bookingRepository.save(booking);

        // 5. Send a message to Kafka that a new booking was created
        kafkaTemplate.send("booking-events", savedBooking);

        return savedBooking;
    }

    @KafkaListener(topics = "payment-events", groupId = "booking-group")
    public void consumePaymentStatus(PaymentStatusEvent event) {
        LOGGER.info("Received payment status event -> {}", event.toString());

        // Find the booking and update its status
        bookingRepository.findById(event.bookingId()).ifPresent(booking -> {
            if ("SUCCESS".equals(event.status())) {
                booking.setStatus("CONFIRMED");
                bookingRepository.save(booking);
                LOGGER.info("Booking {} confirmed.", booking.getId());
            }
            // You could also handle a "FAILED" status here
        });
    }
}
