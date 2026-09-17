package com.ticketShop.repository;

import com.ticketShop.model.entity.TicketDetail;

import java.util.Optional;

public interface TicketDetailRepository {
    Optional<TicketDetail> findById(Long id);
}
