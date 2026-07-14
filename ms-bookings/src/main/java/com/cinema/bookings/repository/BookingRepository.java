package com.cinema.bookings.repository;

import java.util.List;

import com.cinema.bookings.entity.Booking;

public interface BookingRepository extends JparRepository<Booking, Long> {

    Booking save(Booking booking);

    List<Booking> findAll();

    Booking findById(Long id);
    
}
