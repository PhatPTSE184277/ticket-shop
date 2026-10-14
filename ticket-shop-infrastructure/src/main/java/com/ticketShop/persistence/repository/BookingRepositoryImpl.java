package com.ticketShop.persistence.repository;

import com.ticketShop.model.entity.Booking;
import com.ticketShop.persistence.mapper.BookingJPAMapper;
import com.ticketShop.repository.BookingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class BookingRepositoryImpl implements BookingRepository {
    @Autowired
    private BookingJPAMapper bookingJPAMapper;

    @Override
    public Booking save(Booking booking) {
        log.info("Saving booking for ticket: {}", booking.getTicketId());
        return bookingJPAMapper.save(booking);
    }

    @Override
    public Optional<Booking> findByBookingCode(String bookingCode) {
        return bookingJPAMapper.findByBookingCode(bookingCode);
    }
}
