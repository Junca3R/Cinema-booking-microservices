package com.cinema.bookings.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cinema.bookings.dto.ShowtimeDTO;

// Apuntamos al nombre del microservicio y su URL local temporal
@FeignClient(name = "ms-cinema-catalog", url = "http://localhost:8086")
public interface CatalogClient {

    @GetMapping("/api/v1/showtimes/{id}")//GetMapping para obtener los detalles de una función específica por su ID
    ShowtimeDTO getShowtimeById(@PathVariable("id") Long id);

    @PutMapping("/api/v1/showtimes/{id}/seats")//putMapping para reservar asientos en una función específica por su ID y la cantidad de asientos a reservar
    ShowtimeDTO reserveSeats(@PathVariable("id") Long id, @RequestParam("seats") Integer seats);
}