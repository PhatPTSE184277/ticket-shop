package com.ticketShop.service;

import com.ticketShop.model.entity.TicketDetail;

import java.util.Optional;

public interface TicketDetailDomainService {
    TicketDetail getTicketDetailById(Long id);
}
