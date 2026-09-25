package com.ticketShop.service;

import com.ticketShop.model.entity.TicketDetail;

import java.util.List;
import java.util.Optional;

public interface TicketDetailDomainService {
    TicketDetail getTicketDetailById(Long detailId);

    List<TicketDetail>
}
