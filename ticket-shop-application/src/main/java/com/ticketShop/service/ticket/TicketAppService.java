package com.ticketShop.service.ticket;

import com.ticketShop.model.command.CreateTicketCommand;
import com.ticketShop.model.dto.TicketDTO;

import java.util.List;

public interface TicketAppService {
    TicketDTO getTicketById(Long id, Long version);

    List<TicketDTO> getTicketsByEventId(Long eventId);

    /**
     * Tạo ticket mới
     * @param createTicketCommand Request từ Controller
     * @return TicketDTO
     */
    TicketDTO createTicket(CreateTicketCommand createTicketCommand);
}

