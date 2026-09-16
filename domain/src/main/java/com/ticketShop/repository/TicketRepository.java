package com.ticketShop.repository;

import com.ticketShop.model.entity.Ticket;

import java.util.List;

public interface TicketRepository {

    /**
     * Lấy tất cả ticket đang active
     * @return List<Ticket>
     */
    List<Ticket> getAllActiveTickets();
}
