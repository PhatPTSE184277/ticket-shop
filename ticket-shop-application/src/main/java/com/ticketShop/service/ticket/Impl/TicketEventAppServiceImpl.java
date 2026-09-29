package com.ticketShop.service.ticket.Impl;

import com.ticketShop.mapper.TicketEventMapper;
import com.ticketShop.model.dto.response.TicketEventResponse;
import com.ticketShop.model.entity.TicketEvent;
import com.ticketShop.service.ticket.TicketEventAppService;
import com.ticketShop.service.ticket.cache.TicketEventCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TicketEventAppServiceImpl implements TicketEventAppService {
    private static final String TICKET_EVENT_CACHE_PREFIX = "PRO_TICKET:EVENT:";
    private static final String TICKET_ITEM_CACHE_PREFIX = "PRO_TICKET:ITEM:";

    @Autowired
    private TicketEventCacheService ticketEventCacheService;

    @Autowired
    private TicketEventMapper ticketEventMapper;

    @Override
    public List<TicketEventResponse> getAllActiveTicketEvents() {

        log.info("App Service: Getting all active ticket events");

        List<TicketEvent> ticketEvents = ticketEventCacheService.getActiveTicketEvents();

        return ticketEvents.stream()
                .map(ticketEventMapper::toResponse)
                .toList();
    }
}
