package com.ticketShop.service.ticket;

import com.ticketShop.model.dto.TicketItemDTO;

import java.util.List;

public interface TicketItemAppService {
    TicketItemDTO getTicketItemById(Long id, Long version);

    List<TicketItemDTO> getTicketItemsByEventId(Long eventId);
}
