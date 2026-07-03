package com.cinema.catalog.service;

import java.util.List;

import com.cinema.catalog.entity.Movie;

public interface MovieService {

    Movie saveMovie(Movie movie);
    List<Movie> getAllMovies();
    Movie getMovieById(Long id);
    
}
