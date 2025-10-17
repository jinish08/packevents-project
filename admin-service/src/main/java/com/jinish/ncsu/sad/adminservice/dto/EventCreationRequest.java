package com.jinish.ncsu.sad.adminservice.dto;

import java.time.LocalDateTime;
import java.util.List;

// Using a record is a concise way to create a DTO
public record EventCreationRequest(
        String title,
        String description,
        String location,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        List<SeatCreationRequest> seats
) {}