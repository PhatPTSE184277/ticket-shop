package com.ticketShop.repository;

import com.ticketShop.model.entity.TicketItem;

import java.util.List;
import java.util.Optional;

public interface TicketItemRepository {
    Optional<TicketItem> findById(Long id);

    List<TicketItem> findByEventId(Long eventId);

    /**
     * Lưu TicketItem mới
     * @param ticketItem
     * @return TicketDetail được lưu
     */
    TicketItem save(TicketItem ticketItem);
}
