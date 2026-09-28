package com.ticketShop.service.ticket;

import com.ticketShop.model.dto.response.TicketItemResponse;

import java.util.List;

public interface TicketItemAppService {
    TicketItemResponse getTicketItemById(Long id, Long version);

    List<TicketItemResponse> getTicketItemsByEventId(Long eventId);
}
