package com.jinish.ncsu.sad.adminservice.service;

import com.jinish.ncsu.sad.adminservice.dto.EventCreationRequest;
import com.jinish.ncsu.sad.adminservice.model.Event;
import com.jinish.ncsu.sad.adminservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final EventRepository eventRepository;

    public Event createEvent(EventCreationRequest request) {
        // Map the DTO to an Event entity
        Event newEvent = new Event();
        newEvent.setTitle(request.title());
        newEvent.setDescription(request.description());
        newEvent.setLocation(request.location());
        newEvent.setStartDateTime(request.startDateTime());
        newEvent.setEndDateTime(request.endDateTime());

        return eventRepository.save(newEvent);
    }
}