package com.ticketShop.service.Impl;

import com.ticketShop.model.entity.Ticket;
import com.ticketShop.model.enums.TicketStatus;
import com.ticketShop.repository.TicketRepository;
import com.ticketShop.service.TicketDomainService;
import com.ticketShop.validator.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketDomainServiceImpl implements TicketDomainService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketValidator ticketValidator;

    @Override
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }

    @Override
    public List<Ticket> getTicketsByEventId(Long eventId) {
        return ticketRepository.findByEventId(eventId);
    }

    @Override
    public Ticket createTicket(Ticket ticket) {
        // 1. Validate Ticket information
        ticketValidator.validateTicketCreation(ticket);

        // 2. Set audit fields for Ticket
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.setStatus(TicketStatus.INACTIVE);

        return ticketRepository.save(ticket);
    }
}
