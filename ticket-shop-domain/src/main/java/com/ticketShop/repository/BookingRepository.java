package com.ticketShop.repository;

import com.ticketShop.model.entity.Booking;

import java.util.Optional;

public interface BookingRepository {
    Booking save(Booking booking);
    Optional<Booking> findByBookingCode(String bookingCode);
}