package com.jinish.ncsu.sad.paymentservice.service;

import com.jinish.ncsu.sad.paymentservice.dto.BookingDto;
import com.jinish.ncsu.sad.paymentservice.dto.PaymentStatusEvent;
import com.jinish.ncsu.sad.paymentservice.model.Payment;
import com.jinish.ncsu.sad.paymentservice.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    @KafkaListener(topics = "booking-events", groupId = "payment-group")
    public void consume(BookingDto bookingDto) {
        LOGGER.info("Received booking event -> {}", bookingDto.toString());

        PaymentStatusEvent event;
        try {
            long amountInCents = 5000L; // $50.00

            PaymentIntentCreateParams params =
                    PaymentIntentCreateParams.builder()
                            .setAmount(amountInCents)
                            .setCurrency("usd")
                            .setAutomaticPaymentMethods(
                                    PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                            .setEnabled(true)
                                            // Explicitly tell Stripe to never redirect for this payment
                                            .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                                            .build())
                            .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            PaymentIntent confirmedPaymentIntent =
                    paymentIntent.confirm(
                            com.stripe.param.PaymentIntentConfirmParams.builder()
                                    .setPaymentMethod("pm_card_visa")
                                    .build());

            LOGGER.info("Stripe PaymentIntent succeeded with ID: {}", confirmedPaymentIntent.getId());
            event = new PaymentStatusEvent(bookingDto.id(), "SUCCESS");

        } catch (StripeException e) {
            LOGGER.error("Stripe error for booking ID {}: {}", bookingDto.id(), e.getMessage());
            event = new PaymentStatusEvent(bookingDto.id(), "FAILED");
        }

        kafkaTemplate.send("payment-events", event);
        LOGGER.info("Sent payment status event -> {}", event.toString());
    }
}
