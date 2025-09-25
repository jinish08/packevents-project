package com.jinish.ncsu.sad.bookingservice.repository;

import com.jinish.ncsu.sad.bookingservice.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {}
