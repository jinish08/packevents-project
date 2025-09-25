package com.jinish.ncsu.sad.bookingservice.config;

import com.jinish.ncsu.sad.bookingservice.model.Event;
import com.jinish.ncsu.sad.bookingservice.model.Seat;
import com.jinish.ncsu.sad.bookingservice.repository.EventRepository;
import com.jinish.ncsu.sad.bookingservice.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only add data if the repository is empty
        if (eventRepository.count() == 0) {
            Event event1 = new Event();
            event1.setTitle("Tech Conference 2025");
            event1.setDescription("Annual tech conference with the best speakers.");
            event1.setLocation("Raleigh Convention Center");
            event1.setStartDateTime(LocalDateTime.of(2025, 10, 20, 9, 0));
            event1.setEndDateTime(LocalDateTime.of(2025, 10, 22, 17, 0));

            Event event2 = new Event();
            event2.setTitle("Live Music Fest");
            event2.setDescription("A weekend of live music from various artists.");
            event2.setLocation("Red Hat Amphitheater");
            event2.setStartDateTime(LocalDateTime.of(2025, 11, 5, 18, 0));
            event2.setEndDateTime(LocalDateTime.of(2025, 11, 7, 23, 0));

            eventRepository.saveAll(List.of(event1, event2));

            System.out.println("--- Sample events seeded ---");

            // --- ADD SEATS FOR EVENT 1 ---
            for (int i = 1; i <= 5; i++) {
                Seat seat = new Seat();
                seat.setEventId(event1.getId());
                seat.setSeatNumber("A" + i);
                seat.setCategory("Standard");
                seatRepository.save(seat);
            }

            // --- ADD SEATS FOR EVENT 2 ---
            for (int i = 1; i <= 5; i++) {
                Seat seat = new Seat();
                seat.setEventId(event2.getId());
                seat.setSeatNumber("V" + i);
                seat.setCategory("VIP");
                seatRepository.save(seat);
            }
            System.out.println("--- Sample seats seeded ---");
        }
    }
}
