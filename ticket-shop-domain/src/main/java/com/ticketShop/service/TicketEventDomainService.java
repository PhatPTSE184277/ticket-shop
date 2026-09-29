package com.ticketShop.service;

import com.ticketShop.model.entity.TicketEvent;

import java.util.List;

public interface TicketEventDomainService {
    List<TicketEvent> getAllActiveTicketEvents();
}
