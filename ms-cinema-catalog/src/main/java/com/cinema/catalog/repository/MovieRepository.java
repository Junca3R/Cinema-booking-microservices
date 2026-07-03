package com.cinema.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cinema.catalog.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {// iterface para manejar la entidad Movie, hereda de JpaRepository para tener acceso a métodos CRUD y consultas personalizadas.
}