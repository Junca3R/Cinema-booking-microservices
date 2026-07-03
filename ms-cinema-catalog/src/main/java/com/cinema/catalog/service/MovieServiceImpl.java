package com.cinema.catalog.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cinema.catalog.entity.Movie;
import com.cinema.catalog.repository.MovieRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService{// Implementación de la interfaz MovieService, que define los métodos para manejar la entidad Movie. Esta clase utiliza MovieRepository para interactuar con la base de datos.
    
    private final MovieRepository movieRepository;// Inyección de dependencia de MovieRepository para acceder a los métodos CRUD y consultas personalizadas.
    
    @Override
    public Movie saveMovie(Movie movie){
        return movieRepository.save(movie);// Guarda una película en la base de datos y devuelve la entidad guardada.

    }

    @Override
    public List<Movie> getAllMovies(){//list para obtener todas las películas de la base de datos.
        return movieRepository.findAll();// Obtiene todas las películas de la base de datos y devuelve una lista de entidades Movie.


    }

    @Override
    public Movie getMovieById(Long id){// Obtiene una película específica por su ID
        return movieRepository.findById(id).orElse(null);


    }


}
