package com.cinema.bookings.service;

import java.util.List;

import com.cinema.bookings.entity.Booking;

public interface BookingService {
    Booking createBooking(Booking booking);
    List<Booking> getAllBookings();
    Booking getBookingById(Long id);
}