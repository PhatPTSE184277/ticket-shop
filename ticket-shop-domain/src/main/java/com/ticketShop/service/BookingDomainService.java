package com.ticketShop.service;

import com.ticketShop.model.entity.Booking;

public interface BookingDomainService {
    Booking createBooking(Long ticketId, int quantity);
}