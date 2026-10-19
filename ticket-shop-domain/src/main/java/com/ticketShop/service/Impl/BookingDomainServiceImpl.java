package com.ticketShop.service.Impl;

import com.ticketShop.model.entity.Booking;
import com.ticketShop.repository.BookingRepository;
import com.ticketShop.service.BookingDomainService;
import com.ticketShop.validator.BookingValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class BookingDomainServiceImpl implements BookingDomainService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingValidator  bookingValidator;

    @Override
    public Booking createBooking(Long ticketId, int quantity) {
       bookingValidator.validateCreateBooking(ticketId, quantity);

        Booking booking = new Booking();
        booking.setTicketId(ticketId);
        booking.setQuantity(quantity);
        booking.setBookingCode(generateBookingCode());
        booking.setCreatedAt(LocalDateTime.now());

        Booking saved = bookingRepository.save(booking);
        log.info("Created booking: {} for ticket: {}", saved.getBookingCode(), ticketId);
        return saved;
    }

    private String generateBookingCode() {
        return "BK" + System.currentTimeMillis()
                + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }
}