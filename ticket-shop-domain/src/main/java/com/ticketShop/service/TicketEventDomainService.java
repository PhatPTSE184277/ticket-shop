package com.ticketShop.service;

import com.ticketShop.model.entity.TicketEvent;
import com.ticketShop.model.entity.TicketItem;

import java.util.List;


public interface TicketEventDomainService {
    List<TicketEvent> getAllActiveTicketEvents();

    /**
     * Tạo ticket event mới + TicketItem ban đầu
     *
     * Business Logic:
     * - Validate ticket event information
     * - Generate unique ticket event ID
     * - Create default TicketItem
     *
     * @param ticketEvent TicketEvent entity
     * @param ticketItem Default TicketItem
     * @return TicketEvent đã được lưu (kèm ID)
     * @throws IllegalArgumentException nếu validation thất bại
     */
    TicketEvent createTicket(TicketEvent ticketEvent, TicketItem ticketItem);
}
