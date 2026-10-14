package com.ticketShop.persistence.mapper;

import com.ticketShop.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingJPAMapper extends JpaRepository<Booking, Long> {
    Optional<Booking> findByBookingCode(String bookingCode);
}