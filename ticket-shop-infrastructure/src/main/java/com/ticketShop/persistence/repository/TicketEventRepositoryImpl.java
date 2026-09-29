package com.ticketShop.persistence.repository;

import com.ticketShop.model.entity.TicketEvent;
import com.ticketShop.model.enums.TicketEventStatus;
import com.ticketShop.persistence.mapper.TicketEventJPAMapper;
import com.ticketShop.repository.TicketEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TicketEventRepositoryImpl implements TicketEventRepository {
    @Autowired
    private TicketEventJPAMapper ticketEventJPAMapper;

    @Override
    public List<TicketEvent> findAllActive() {
        return ticketEventJPAMapper.findByStatus(TicketEventStatus.ACTIVE);
    }

    @Override
    public TicketEvent save(TicketEvent ticketEvent) {
        return null;
    }
}
