package com.ticketShop.service;


import com.ticketShop.model.entity.Ticket;

import java.util.List;

public interface TicketDomainService {
    Ticket getTicketById(Long id);

    List<Ticket> getTicketsByEventId(Long eventId);

    Ticket createTicket(Ticket ticket);
}
