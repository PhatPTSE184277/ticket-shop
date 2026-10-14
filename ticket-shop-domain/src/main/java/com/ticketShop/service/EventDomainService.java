package com.ticketShop.service;

import com.ticketShop.model.entity.Event;
import com.ticketShop.model.entity.Ticket;

import java.util.List;


public interface EventDomainService {
    List<Event> getAllActiveEvents();

    /**
     * Tạo event mới + Ticket ban đầu
     *
     * Business Logic:
     * - Validate event information
     * - Generate unique event ID
     * - Create default Ticket
     *
     * @param event Event entity
     * @param ticket Default Ticket
     * @return Event đã được lưu (kèm ID)
     * @throws IllegalArgumentException nếu validation thất bại
     */
    Event createEvent(Event event, Ticket ticket);
}
