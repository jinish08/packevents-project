package com.jinish.ncsu.sad.paymentservice.model;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String bookingId;
    private double amount; // You'd get this from the booking event
    private String status; // PENDING, SUCCESS, FAILED
    private LocalDateTime paymentDate;
}