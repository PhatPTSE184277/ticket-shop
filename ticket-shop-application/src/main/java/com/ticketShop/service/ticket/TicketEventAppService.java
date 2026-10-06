package com.ticketShop.service.ticket;

import com.ticketShop.model.command.CreateTicketEventCommand;
import com.ticketShop.model.command.CreateTicketItemCommand;
import com.ticketShop.model.dto.TicketEventDTO;

import java.util.List;

public interface TicketEventAppService {
    List<TicketEventDTO> getAllActiveTicketEvents();

    /**
     * Tạo ticketEvent mới
     * @param createEventCommand Request từ Controller
     * @param createItemCommand TicketItem request
     * @return TicketEventResponse
     */
    TicketEventDTO createTicketEvent(CreateTicketEventCommand createEventCommand, CreateTicketItemCommand createItemCommand);
}
