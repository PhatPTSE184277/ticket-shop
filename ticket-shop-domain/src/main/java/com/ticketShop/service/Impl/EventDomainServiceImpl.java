package com.ticketShop.service.Impl;

import com.ticketShop.model.entity.Event;
import com.ticketShop.model.entity.Ticket;
import com.ticketShop.model.enums.EventStatus;
import com.ticketShop.model.enums.TicketStatus;
import com.ticketShop.repository.EventRepository;
import com.ticketShop.repository.TicketRepository;
import com.ticketShop.service.EventDomainService;
import com.ticketShop.validator.EventValidator;
import com.ticketShop.validator.TicketValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class EventDomainServiceImpl implements EventDomainService {
    @Autowired
    EventRepository eventRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    EventValidator eventValidator;

    @Autowired
    TicketValidator ticketValidator;

    @Override
    public List<Event> getAllActiveEvents() {
        return eventRepository.findAllActive();
    }

    @Override
    public Event createEvent(Event event, Ticket ticket) {
        // 1. Validate Event information
        eventValidator.validateEventCreation(event);

        // 2. Set audit fields for Event
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());
        event.setStatus(EventStatus.INACTIVE);

        // 3. Persist Event
        Event savedEvent = eventRepository.save(event);

        // 4. Create default Event
        ticket.setEventId(savedEvent.getId());
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.setStatus(TicketStatus.INACTIVE);

        // Set defaults for nullable fields that DB requires NOT NULL
        if (ticket.getPriceFlash() == null){
            ticket.setPriceFlash(ticket.getPriceOriginal() != null ? ticket.getPriceOriginal().multiply(BigDecimal.valueOf(0.7)) : BigDecimal.ZERO);
        }
        if (ticket.getSaleStartTime() == null){
            ticket.setSaleStartTime(LocalDateTime.now());
        }
        if (ticket.getSaleEndTime() == null){
            ticket.setSaleEndTime(ticket.getSaleStartTime().plusDays(30));
        }

        // Validate Ticket
        ticketValidator.validateTicketCreation(ticket);

        // 5. Persist Ticket
        ticketRepository.save(ticket);
        log.info("Created event with ID: {} and default Ticket", savedEvent.getId());

        return savedEvent;
    }
}
