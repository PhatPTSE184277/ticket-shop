package com.ticketShop.persistence.mapper;

import com.ticketShop.model.entity.TicketItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketItemJPAMapper extends JpaRepository<TicketItem, Long> {
    Optional<TicketItem> findById(Long id);
}
