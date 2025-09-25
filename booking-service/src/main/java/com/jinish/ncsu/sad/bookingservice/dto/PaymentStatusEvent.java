package com.jinish.ncsu.sad.bookingservice.dto;

public record PaymentStatusEvent(String bookingId, String status) {
}
