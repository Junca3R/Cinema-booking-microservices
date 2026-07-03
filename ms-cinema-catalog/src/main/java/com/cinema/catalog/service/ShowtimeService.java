package com.cinema.catalog.service;

import java.util.List;

import com.cinema.catalog.entity.Showtime;

public interface  ShowtimeService {

    Showtime saveShowtime(Showtime showtime);// Método para guardar una nueva función
    List<Showtime> getAllShowtimes();// Método para obtener todas las funciones
    List<Showtime> getShowtimesByMovieId(Long movieId);// Método para obtener todas las funciones de una película específica por su ID
    Showtime getShowtimeById(Long id);// Método para obtener una función específica por su ID
    Showtime updateAvailableSeats(Long id, Integer seatsToReserve);// Método para actualizar el número de asientos disponibles en una función específica



    
}
