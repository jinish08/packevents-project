package com.jinish.ncsu.sad.bookingservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seats")
@Data
@NoArgsConstructor
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String eventId; // Which event this seat belongs to
    private String seatNumber; // e.g., "A12"
    private String category; // e.g., "VIP", "Standard"
    private boolean isAvailable = true;
}
