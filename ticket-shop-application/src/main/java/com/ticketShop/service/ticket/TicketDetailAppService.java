package com.ticketShop.service.ticket;

import com.ticketShop.model.TicketDetailDTO;

public interface TicketDetailAppService {
    TicketDetailDTO getTicketDetailById(Long id, Long version);
}
