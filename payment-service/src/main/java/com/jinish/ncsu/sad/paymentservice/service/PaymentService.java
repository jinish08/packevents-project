package com.jinish.ncsu.sad.paymentservice.service;

import com.jinish.ncsu.sad.paymentservice.dto.BookingDto;
import com.jinish.ncsu.sad.paymentservice.dto.PaymentStatusEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // Add this
public class PaymentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);
    private final KafkaTemplate<String, PaymentStatusEvent> kafkaTemplate; // Inject KafkaTemplate

    @KafkaListener(topics = "booking-events", groupId = "payment-group")
    public void consume(BookingDto bookingDto) {
        LOGGER.info("Received booking event -> {}", bookingDto.toString());

        // 1. Simulate payment processing
        LOGGER.info("Processing payment for booking ID: {}", bookingDto.id());
        // (In a real app, you'd interact with a payment gateway here)

        // 2. Create the result event
        PaymentStatusEvent event = new PaymentStatusEvent(bookingDto.id(), "SUCCESS");

        // 3. Send the result to a new "payment-events" topic
        kafkaTemplate.send("payment-events", event);
        LOGGER.info("Sent payment status event -> {}", event.toString());
    }
}
