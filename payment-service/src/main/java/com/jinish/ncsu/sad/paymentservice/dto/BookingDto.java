package com.jinish.ncsu.sad.paymentservice.dto;

import java.util.List;

// This record must have fields that match the JSON from the booking service
public record BookingDto(String id, String userId, String eventId, List<String> seatIds, String status) {
}
