package com.jinish.ncsu.sad.bookingservice.dto;

import lombok.Data;
import java.util.List;

@Data
public class BookingRequest {
    private String eventId;
    private String userId; // We'll get this from the security context later
    private List<String> seatIds;
}
