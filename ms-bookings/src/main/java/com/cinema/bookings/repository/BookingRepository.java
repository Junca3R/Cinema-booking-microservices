package com.cinema.bookings.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinema.bookings.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

}
