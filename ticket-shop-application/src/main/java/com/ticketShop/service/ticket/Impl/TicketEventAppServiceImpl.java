package com.ticketShop.service.ticket.Impl;

import com.ticketShop.cache.RedisInfraService;
import com.ticketShop.mapper.TicketEventMapper;
import com.ticketShop.mapper.TicketItemMapper;
import com.ticketShop.model.command.CreateTicketEventCommand;
import com.ticketShop.model.command.CreateTicketItemCommand;
import com.ticketShop.model.dto.TicketEventDTO;
import com.ticketShop.model.entity.TicketEvent;
import com.ticketShop.model.entity.TicketItem;
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
    private static final String TICKET_EVENT_CACHE_PREFIX = "PRO_TICKET:EVENT:";
    private static final String TICKET_ITEM_CACHE_PREFIX = "PRO_TICKET:ITEM:";

    @Autowired
    private TicketEventCacheService ticketEventCacheService;

    @Autowired
    private TicketEventDomainService ticketEventDomainService;

    @Autowired
    private TicketEventMapper ticketEventMapper;

    @Autowired
    private TicketItemMapper ticketItemMapper;

    @Autowired
    private RedisInfraService redisInfraService;

    @Override
    public List<TicketEventDTO> getAllActiveTicketEvents() {
        List<TicketEvent> ticketEvents = ticketEventCacheService.getActiveTicketEvents();

        if (ticketEvents == null || ticketEvents.isEmpty()) {
            return Collections.emptyList();
        }

        return ticketEvents.stream()
                .filter(Objects::nonNull)
                .map(ticketEventMapper::toDTO)
                .toList();
    }

    @Override
    public TicketEventDTO createTicketEvent(CreateTicketEventCommand ticketEventCommand, CreateTicketItemCommand createItemCommand) {
        //1.Convert Request -> Entity (via Mapper)
        TicketEvent ticketEvent = ticketEventMapper.toEntity(ticketEventCommand);
        TicketItem ticketItem = ticketItemMapper.toEntity(createItemCommand);

        // 2. Call Domain Service (với business logic validation + persist)
        TicketEvent createdTicket = ticketEventDomainService.createTicket(ticketEvent, ticketItem);

        // 3. Write-Through Cache: set Redis ngay sau khi DB thành công
        this.cacheTicketEvent(ticketEvent);
        this.cacheTicketItem(ticketItem);// ticketItem đã có ID từ JPA save

        log.info("Created & cached ticket event ID: {}, ticketItem ID: {}",
                createdTicket.getId(), ticketItem.getId());

        // 4. Convert Entity → DTO
        return ticketEventMapper.toDTO(createdTicket);
    }

    // ========== CACHE METHODS ==========

    /**
     * Cache Ticket Event entity vào Redis
     */
    private void cacheTicketEvent(TicketEvent ticketEvent){
        try {
            log.info("Caching ticket event ID: {}, name: {}", ticketEvent.getId(), ticketEvent.getName());
            if (ticketEvent.getId() != null) {
                redisInfraService.setObject(TICKET_EVENT_CACHE_PREFIX + ticketEvent.getId(), ticketEvent);
            }
        }catch (Exception e){
            // Cache failure không được block business flow
            log.warn("Failed to cache ticket ID: {}, error: {}", ticketEvent.getId(), e.getMessage());
        }
    }

    /**
     * Cache TicketItem entity vào Redis
     * Sử dụng cùng key convention với TicketItemCacheService
     */
    private void cacheTicketItem(TicketItem ticketItem){
        try {
            log.info("Caching ticket item ID: {}, name: {}", ticketItem.getId(), ticketItem.getName());
            if (ticketItem.getId() != null) {
                redisInfraService.setObject(TICKET_ITEM_CACHE_PREFIX + ticketItem.getId(), ticketItem);
            }
        }catch (Exception e){
            log.warn("Failed to cache ticket item ID: {}, error: {}", ticketItem.getId(), e.getMessage());
        }
    }

    /**
     * Xóa cache TicketEvent khi delete
     */
    private void evictTicketEventCache(TicketEvent ticketEvent){
        try {
            redisInfraService.delete(TICKET_EVENT_CACHE_PREFIX + ticketEvent.getId());
            // Lưu ý: không xóa TICKET_ITEM vì không biết item ID từ ticket event ID
            // Cần query hoặc maintain mapping nếu muốn xóa cả detail cache
        }catch (Exception e){
            log.warn("Failed to evict ticket event ID: {}, error: {}", ticketEvent.getId(), e.getMessage());
        }
    }
}
