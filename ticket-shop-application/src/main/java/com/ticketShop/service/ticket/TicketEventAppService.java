package com.ticketShop.service.ticket;

import com.ticketShop.model.dto.response.TicketEventResponse;

import java.util.List;

public interface TicketEventAppService {
    List<TicketEventResponse> getAllActiveTicketEvents();
}
