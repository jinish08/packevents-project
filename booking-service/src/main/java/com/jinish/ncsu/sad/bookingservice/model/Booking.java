package com.jinish.ncsu.sad.bookingservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String userId;
    private String eventId;
    @ElementCollection(fetch = FetchType.EAGER) // Stores a simple collection of strings
    private List<String> seatIds;
    private String status; // e.g., "PENDING", "CONFIRMED", "CANCELLED"
    private LocalDateTime bookingTime;

    @PrePersist
    protected void onCreate() {
        bookingTime = LocalDateTime.now();
    }
}