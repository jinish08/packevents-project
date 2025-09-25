package com.jinish.ncsu.sad.notificationservice.dto;

import java.util.List;

public record BookingDto(String id, String userId, String eventId, List<String> seatIds, String status) {
}
