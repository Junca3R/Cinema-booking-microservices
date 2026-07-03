package com.cinema.catalog.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cinema.catalog.entity.Showtime;
import com.cinema.catalog.repository.ShowtimeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShowtimeServiceImpl implements ShowtimeService{

private final ShowtimeRepository showtimeRepository;

@Override
public Showtime saveShowtime(Showtime showtime) {
    return showtimeRepository.save(showtime);

}
@Override
public List<Showtime> getAllShowtimes(){
    return showtimeRepository.findAll();
}

@Override
public List<Showtime>getShowtimesByMovieId(Long id){
    return showtimeRepository.findByMovieId(id);
}

@Override
public Showtime getShowtimeById(Long id){
    return showtimeRepository.findById(id).orElse(null);
}
@Override
    public Showtime updateAvailableSeats(Long id, Integer seatsToReserve) {
        Showtime showtime = getShowtimeById(id);// Obtenemos la función por su ID
        
        if (showtime.getAvailableSeats() < seatsToReserve) {// Verificamos si hay suficientes asientos disponibles
            throw new RuntimeException("No hay suficientes asientos disponibles para esta función.");// Si no hay suficientes asientos, lanzamos una excepción
        }
        
        // Restamos los asientos solicitados
        showtime.setAvailableSeats(showtime.getAvailableSeats() - seatsToReserve);// Actualizamos el número de asientos disponibles
        return showtimeRepository.save(showtime);// Guardamos la función actualizada y la devolvemos
    }




    
    

    
}
