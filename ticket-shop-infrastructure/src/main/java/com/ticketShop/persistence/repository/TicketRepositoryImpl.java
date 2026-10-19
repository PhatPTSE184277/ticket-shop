package com.ticketShop.persistence.repository;

import com.ticketShop.model.entity.Ticket;
import com.ticketShop.model.enums.TicketStatus;
import com.ticketShop.persistence.mapper.TicketJPAMapper;
import com.ticketShop.repository.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class TicketRepositoryImpl implements TicketRepository {
    @Autowired
    private TicketJPAMapper ticketJPAMapper;

    @Override
    public Optional<Ticket> findById(Long id) {
        return ticketJPAMapper.findById(id);
    }

    @Override
    public List<Ticket> findByEventId(Long eventId) {
        return ticketJPAMapper.findByEventId(eventId);
    }

    @Override
    public void updateStatusByEventId(Long eventId, int status) {

    }

    @Override
    public Ticket save(Ticket ticket) {
        log.info("Saving ticket: {}", ticket.getName());
        return ticketJPAMapper.save(ticket);
    }


    //Check
    @Override
    public boolean existsByEventIdAndName(Long eventId, String name) {
        return ticketJPAMapper.existsByEventIdAndName(eventId, name);
    }

    @Override
    public boolean existsByIdAndStatus(Long ticketId, TicketStatus status) {
        return ticketJPAMapper.existsByIdAndStatus(ticketId, status);
    }

}
