package com.jinish.ncsu.sad.paymentservice.service;

import com.jinish.ncsu.sad.paymentservice.dto.BookingDto;
import com.jinish.ncsu.sad.paymentservice.dto.PaymentStatusEvent;
import com.jinish.ncsu.sad.paymentservice.model.Payment;
import com.jinish.ncsu.sad.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor // Add this
public class PaymentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);
    private final KafkaTemplate<String, PaymentStatusEvent> kafkaTemplate; // Inject KafkaTemplate
    private final PaymentRepository paymentRepository;

    @KafkaListener(topics = "booking-events", groupId = "payment-group")
    public void consume(BookingDto bookingDto) {

        LOGGER.info("Received booking event -> {}", bookingDto.toString());

        // 1. Create and save an initial Payment record
        Payment payment = new Payment();
        payment.setBookingId(bookingDto.id());
        // In a real app, you'd calculate the amount
        payment.setAmount(50.00); // Example amount
        payment.setStatus("PENDING");
        paymentRepository.save(payment);

        // 2. Simulate payment processing...
        LOGGER.info("Processing payment for booking ID: {}", bookingDto.id());

        // 3. Update the payment record
        payment.setStatus("SUCCESS");
        payment.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(payment);

        // 4. Send the result event
        PaymentStatusEvent event = new PaymentStatusEvent(bookingDto.id(), "SUCCESS");
        kafkaTemplate.send("payment-events", event);
        LOGGER.info("Sent payment status event -> {}", event.toString());
    }
}
