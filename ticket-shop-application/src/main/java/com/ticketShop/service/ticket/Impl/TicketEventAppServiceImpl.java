package com.ticketShop.service.ticket.Impl;

import com.ticketShop.mapper.TicketEventMapper;
import com.ticketShop.model.dto.response.TicketEventResponse;
import com.ticketShop.model.entity.TicketEvent;
import com.ticketShop.service.TicketEventDomainService;
import com.ticketShop.service.ticket.TicketEventAppService;
import com.ticketShop.service.ticket.cache.TicketEventCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class TicketEventAppServiceImpl implements TicketEventAppService {

    @Autowired
    private TicketEventCacheService ticketEventCacheService;

    @Autowired
    private TicketEventMapper ticketEventMapper;

    @Override
    public List<TicketEventResponse> getAllActiveTicketEvents() {
        List<TicketEvent> ticketEvents = ticketEventCacheService.getActiveTicketEvents();

        if (ticketEvents == null || ticketEvents.isEmpty()) {
            return Collections.emptyList();
        }

        return ticketEvents.stream()
                .filter(Objects::nonNull)
                .map(ticketEventMapper::toResponse)
                .toList();
    }
}
