package com.cinema.bookings.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long showtimeId;      // ID de la función en ms-cinema-catalog
    private String userEmail;     // Email del usuario que reserva (obtenido del ms-auth-users)
    private Integer seatCount;    // Cantidad de asientos reservados
    private Double totalPrice;    // Calculado automáticamente: seatCount * ticketPrice
    private String status;        // CONFIRMADA, CANCELADA
    private LocalDateTime bookingDate;
    
}