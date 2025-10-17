package com.jinish.ncsu.sad.adminservice.model;

import jakarta.persistence.*;
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
