package com.ticketShop.service.ticket;

import com.ticketShop.model.command.CreateEventCommand;
import com.ticketShop.model.command.CreateTicketCommand;
import com.ticketShop.model.dto.EventDTO;

import java.util.List;

public interface EventAppService {
    List<EventDTO> getAllActiveEvents();

    /**
     * Tạo Event mới
     * @param createEventCommand Request từ Controller
     * @param createItemCommand Ticket request
     * @return EventResponse
     */
    EventDTO createEvent(CreateEventCommand createEventCommand, CreateTicketCommand createItemCommand);
}
