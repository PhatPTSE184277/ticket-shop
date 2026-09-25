package com.ticketShop.repository;

import com.ticketShop.model.entity.TicketDetail;

import java.util.List;
import java.util.Optional;

public interface TicketDetailRepository {
    Optional<TicketDetail> findById(Long id);
    List<TicketDetail> findByTicketId(Long ticketId);
}
