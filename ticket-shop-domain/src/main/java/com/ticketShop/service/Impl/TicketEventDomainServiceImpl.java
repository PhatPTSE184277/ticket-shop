package com.ticketShop.service.Impl;

import com.ticketShop.model.entity.TicketEvent;
import com.ticketShop.repository.TicketEventRepository;
import com.ticketShop.service.TicketEventDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TicketEventDomainServiceImpl implements TicketEventDomainService {
    @Autowired
    TicketEventRepository ticketEventRepository;

    @Override
    public List<TicketEvent> getAllActiveTicketEvents() {
        log.info("Domain Service: Getting all active ticket events");
        return ticketEventRepository.findAllActive();
    }
}
