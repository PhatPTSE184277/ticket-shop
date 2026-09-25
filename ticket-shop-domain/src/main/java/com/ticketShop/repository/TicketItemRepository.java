package com.ticketShop.repository;

import com.ticketShop.model.entity.TicketItem;

import java.util.Optional;

public interface TicketItemRepository {
    Optional<TicketItem> findById(Long id);
}
