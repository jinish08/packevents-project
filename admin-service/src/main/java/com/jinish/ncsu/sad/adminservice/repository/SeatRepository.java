package com.jinish.ncsu.sad.adminservice.repository;

import com.jinish.ncsu.sad.adminservice.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, String> {
    List<Seat> findByEventId(String eventId);
}
