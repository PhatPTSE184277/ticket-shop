package com.ticketShop.service.ticket;

import com.ticketShop.model.dto.response.TicketItemResponse;

public interface TicketItemAppService {
    TicketItemResponse getTicketItemById(Long id, Long version);
}
