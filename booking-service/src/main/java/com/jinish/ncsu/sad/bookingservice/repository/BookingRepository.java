package com.jinish.ncsu.sad.bookingservice.repository;

import com.jinish.ncsu.sad.bookingservice.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {}
