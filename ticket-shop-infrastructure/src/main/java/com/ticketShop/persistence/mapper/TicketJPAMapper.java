package com.ticketShop.persistence.mapper;

import com.ticketShop.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketJPAMapper extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findById(Long id);

    List<Ticket> findByEventId(Long eventId);;

    //Check
    boolean existsByEventIdAndName(Long eventId, String name);

}
