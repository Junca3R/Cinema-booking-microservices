package com.cinema.bookings.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cinema.bookings.client.CatalogClient;
import com.cinema.bookings.dto.ShowtimeDTO;
import com.cinema.bookings.entity.Booking;
import com.cinema.bookings.repository.BookingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CatalogClient catalogClient;

    @Override
    public Booking createBooking(Booking booking) {
        // 1. Validar si la función existe consultando al ms-cinema-catalog mediante Feign
        ShowtimeDTO showtime = catalogClient.getShowtimeById(booking.getShowtimeId());
        if (showtime == null) {
            throw new RuntimeException("La función de cine seleccionada no existe.");
        }

        // 2. Validar si hay suficientes asientos en la sala antes de proceder
        if (showtime.getAvailableSeats() < booking.getSeatCount()) {
            throw new RuntimeException("No hay suficientes asientos disponibles. Quedan: " + showtime.getAvailableSeats());
        }

        // 3. Llamar al catálogo por Feign para RESTAR los asientos en la base de datos de cartelera
        catalogClient.reserveSeats(booking.getShowtimeId(), booking.getSeatCount());

        // 4. Calcular el precio total de la compra (Asientos * Precio de la entrada)
        booking.setTotalPrice(showtime.getTicketPrice() * booking.getSeatCount());
        booking.setStatus("CONFIRMADA");
        booking.setBookingDate(LocalDateTime.now());

        // 5. Guardar el registro local de la reserva en Postgres
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

   @Override
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));
    }

    
}