package com.ticketShop.service.ticket;

import com.ticketShop.model.command.CreateTicketEventCommand;
import com.ticketShop.model.command.CreateTicketItemCommand;
import com.ticketShop.model.dto.TicketEventDTO;
import com.ticketShop.model.dto.TicketItemDTO;

import java.util.List;

public interface TicketItemAppService {
    TicketItemDTO getTicketItemById(Long id, Long version);

    List<TicketItemDTO> getTicketItemsByEventId(Long eventId);

    /**
     * Tạo ticketItem mới
     * @param createItemCommand Request từ Controller
     * @return TicketItemDTO
     */
    TicketItemDTO createTicketItem(CreateTicketItemCommand createItemCommand);
}

