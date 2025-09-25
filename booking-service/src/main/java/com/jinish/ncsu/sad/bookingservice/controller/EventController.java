package com.jinish.ncsu.sad.bookingservice.controller;

import com.jinish.ncsu.sad.bookingservice.model.Event;
import com.jinish.ncsu.sad.bookingservice.model.Seat;
import com.jinish.ncsu.sad.bookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(bookingService.getAllEvents());
    }

    @GetMapping("/{eventId}/seats")
    public ResponseEntity<List<Seat>> getSeatsForEvent(@PathVariable String eventId) {
        return ResponseEntity.ok(bookingService.getAvailableSeats(eventId));
    }
}
