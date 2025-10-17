package com.jinish.ncsu.sad.adminservice.controller;

import com.jinish.ncsu.sad.adminservice.dto.EventCreationRequest;
import com.jinish.ncsu.sad.adminservice.model.Event;
import com.jinish.ncsu.sad.adminservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // The request body is now the DTO, not the entity
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody EventCreationRequest request) {
        Event createdEvent = adminService.createEvent(request);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }
}