package com.jinish.ncsu.sad.adminservice.service;

import com.jinish.ncsu.sad.adminservice.dto.EventCreationRequest;
import com.jinish.ncsu.sad.adminservice.model.Event;
import com.jinish.ncsu.sad.adminservice.model.Seat;
import com.jinish.ncsu.sad.adminservice.repository.EventRepository;
import com.jinish.ncsu.sad.adminservice.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;

    public Event createEvent(EventCreationRequest request) {
        // Map the DTO to an Event entity
        Event newEvent = new Event();
        newEvent.setTitle(request.title());
        newEvent.setDescription(request.description());
        newEvent.setLocation(request.location());
        newEvent.setStartDateTime(request.startDateTime());
        newEvent.setEndDateTime(request.endDateTime());

        Event savedEvent = eventRepository.save(newEvent);

        List<Seat> seatEntities = request.seats().stream().map(seatDto -> {
            Seat seat = new Seat();
            seat.setEventId(savedEvent.getId());
            seat.setSeatNumber(seatDto.seatNumber());
            seat.setCategory(seatDto.category());
            seat.setAvailable(true);
            return seat;
        }).toList();

        seatRepository.saveAll(seatEntities);

        return savedEvent;
    }
}