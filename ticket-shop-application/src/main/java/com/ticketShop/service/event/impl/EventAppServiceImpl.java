package com.ticketShop.service.event.impl;

import com.ticketShop.cache.RedisInfraService;
import com.ticketShop.mapper.EventMapper;
import com.ticketShop.mapper.TicketMapper;
import com.ticketShop.model.command.CreateEventCommand;
import com.ticketShop.model.command.CreateTicketCommand;
import com.ticketShop.model.dto.EventDTO;
import com.ticketShop.model.entity.Event;
import com.ticketShop.model.entity.Ticket;
import com.ticketShop.service.EventDomainService;
import com.ticketShop.service.event.EventAppService;
import com.ticketShop.service.event.cache.EventCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class EventAppServiceImpl implements EventAppService {
    private static final String EVENT_CACHE_PREFIX = "PRO_EVENT:";
    private static final String TICKET_CACHE_PREFIX = "PRO_TICKET:";

    @Autowired
    private EventCacheService eventCacheService;

    @Autowired
    private EventDomainService eventDomainService;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private RedisInfraService redisInfraService;

    @Override
    public List<EventDTO> getAllActiveEvents() {
        List<Event> events = eventCacheService.getActiveEvents();

        if (events == null || events.isEmpty()) {
            return Collections.emptyList();
        }

        return events.stream()
                .filter(Objects::nonNull)
                .map(eventMapper::toDTO)
                .toList();
    }

    @Override
    public EventDTO createEvent(CreateEventCommand createEventCommand, CreateTicketCommand createTicketCommand) {
        //1.Convert Request -> Entity (via Mapper)
        Event event = eventMapper.toEntity(createEventCommand);
        Ticket ticket = ticketMapper.toEntity(createTicketCommand);

        // 2. Call Domain Service (với business logic validation + persist)
        Event createdEvent = eventDomainService.createEvent(event, ticket);

        // 3. Write-Through Cache: set Redis ngay sau khi DB thành công
        this.cacheEvent(event);
        this.cacheTicket(ticket);// ticket đã có ID từ JPA save

        log.info("Created & cached event ID: {}, ticket ID: {}",
                createdEvent.getId(), ticket.getId());

        // 4. Convert Entity → DTO
        return eventMapper.toDTO(createdEvent);
    }

    // ========== CACHE METHODS ==========

    /**
     * Cache Event entity vào Redis
     */
    private void cacheEvent(Event event){
        try {
            log.info("Caching event ID: {}, name: {}", event.getId(), event.getName());
            if (event.getId() != null) {
                redisInfraService.setObject(EVENT_CACHE_PREFIX + event.getId(), event);
            }
        }catch (Exception e){
            // Cache failure không được block business flow
            log.warn("Failed to cache event ID: {}, error: {}", event.getId(), e.getMessage());
        }
    }

    /**
     * Cache Ticket entity vào Redis
     * Sử dụng cùng key convention với TicketCacheService
     */
    private void cacheTicket(Ticket ticket){
        try {
            log.info("Caching ticket ID: {}, name: {}", ticket.getId(), ticket.getName());
            if (ticket.getId() != null) {
                redisInfraService.setObject(TICKET_CACHE_PREFIX + ticket.getId(), ticket);
            }
        }catch (Exception e){
            log.warn("Failed to cache ticket ID: {}, error: {}", ticket.getId(), e.getMessage());
        }
    }

    /**
     * Xóa cache Event khi delete
     */
    private void evictEventCache(Event event){
        try {
            redisInfraService.delete(EVENT_CACHE_PREFIX + event.getId());
            // Lưu ý: không xóa TICKET vì không biết ticket ID từ event ID
            // Cần query hoặc maintain mapping nếu muốn xóa cả detail cache
        }catch (Exception e){
            log.warn("Failed to evict event ID: {}, error: {}", event.getId(), e.getMessage());
        }
    }
}
