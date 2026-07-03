package com.cinema.catalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinema.catalog.entity.Showtime;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    List<Showtime> findByMovieId(Long movieId); // Método para obtener todas las funciones de una película específica por su ID
    
}
