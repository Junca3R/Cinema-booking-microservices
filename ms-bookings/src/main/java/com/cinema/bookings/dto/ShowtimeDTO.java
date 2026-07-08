package com.cinema.bookings.dto;

import lombok.Data;

@Data
public class ShowtimeDTO {
    private Long id;
    private String roomName;
    private Integer availableSeats;
    private Double ticketPrice;
}