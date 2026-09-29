package com.ticketShop.repository;

import com.ticketShop.model.entity.TicketEvent;

import java.util.List;

public interface TicketEventRepository {
    List<TicketEvent> findAllActive();

    /**
     * Lưu ticket event mới
     * @param ticketEvent
     * @return Ticket được lưu (kèm ID)
     */
    TicketEvent save(TicketEvent ticketEvent);


}
