package com.cinema.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinema.auth.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User>findByEmlail(String email);
    
}
