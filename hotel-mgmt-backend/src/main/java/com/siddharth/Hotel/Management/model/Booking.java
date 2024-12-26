package com.siddharth.Hotel.Management.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "bookings")
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String userId;
    private Integer eventId;

    private Integer seatsBooked;
    private Double amount;
    private Double GST;
    private Double totalAmount;
    private Boolean isCancelled;
    private Boolean isPartiallyCancelled;
    private Double amountRefunded;
    private String code;
}
