package com.jinish.ncsu.sad.notificationservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinish.ncsu.sad.notificationservice.dto.BookingDto;
import com.jinish.ncsu.sad.notificationservice.dto.PaymentStatusEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);
    private final ObjectMapper objectMapper; // Spring Boot provides this bean

    @KafkaListener(topics = "booking-events")
    public void consumeBookingEvent(String message) {
        try {
            BookingDto bookingDto = objectMapper.readValue(message, BookingDto.class);
            LOGGER.info("Received booking event for user {} -> Status: {}", bookingDto.userId(), bookingDto.status());
            LOGGER.info("--> Sending 'Booking Pending' notification to {}", bookingDto.userId());
        } catch (Exception e) {
            LOGGER.error("Could not deserialize booking event: {}", message, e);
        }
    }

    @KafkaListener(topics = "payment-events")
    public void consumePaymentEvent(String message) {
        try {
            PaymentStatusEvent paymentStatusEvent = objectMapper.readValue(message, PaymentStatusEvent.class);
            LOGGER.info("Received payment event for booking {} -> Status: {}", paymentStatusEvent.bookingId(), paymentStatusEvent.status());
            if ("SUCCESS".equals(paymentStatusEvent.status())) {
                LOGGER.info("--> Sending 'Booking Confirmed' notification for booking {}", paymentStatusEvent.bookingId());
            }
        } catch (Exception e) {
            LOGGER.error("Could not deserialize payment event: {}", message, e);
        }
    }
}