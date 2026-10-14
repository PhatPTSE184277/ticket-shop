package com.ticketShop.service.ticket.Impl;

import com.ticketShop.cache.RedisInfraService;
import com.ticketShop.exception.enums.ResultCode;
import com.ticketShop.mapper.TicketMapper;
import com.ticketShop.model.cache.TicketCache;
import com.ticketShop.model.command.CreateTicketCommand;
import com.ticketShop.model.dto.TicketDTO;
import com.ticketShop.model.entity.Ticket;
import com.ticketShop.service.TicketDomainService;
import com.ticketShop.service.ticket.TicketAppService;
import com.ticketShop.service.ticket.cache.TicketCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ticketShop.exception.ServiceException;

import java.util.List;

@Service
@Slf4j
public class TicketAppServiceImpl implements TicketAppService {
    private static final String TICKET_CACHE_PREFIX = "PRO_TICKET:";

    @Autowired
    private TicketCacheService ticketCacheService;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private TicketDomainService ticketDomainService;

    @Autowired
    private RedisInfraService redisInfraService;

    @Override
    public TicketDTO getTicketById(Long id, Long version) {
        log.info("Implement Application : {}, {}: ", id, version);
        TicketCache ticketCache = ticketCacheService.getTicket(id, version);
        if (ticketCache == null || ticketCache.getTicket() == null) {
            log.warn("Ticket detail not found or lock acquire timeout for id: {}", id);
            throw new ServiceException(ResultCode.TICKET_NOT_EXIST);
        }
        // mapper to DTO
        TicketDTO ticketDTO = ticketMapper.toDTO(ticketCache.getTicket());
        ticketDTO.setVersion(ticketCache.getVersion());
        return ticketDTO;
    }

    @Override
    public List<TicketDTO> getTicketsByEventId(Long eventId) {
        List<TicketCache> ticketCaches = ticketCacheService.getTicketsByEvent(eventId
        );

        if (ticketCaches == null  || ticketCaches.isEmpty()) {
            throw new ServiceException(ResultCode.EVENT_NOT_EXIST);
        }
        return ticketCaches.stream()
                .map(TicketCache::getTicket)
                .map(ticketMapper::toDTO)
                .toList();
    }

    @Override
    public TicketDTO createTicket(CreateTicketCommand createItemCommand) {
        //1.Convert Request -> Entity (via Mapper)
        Ticket ticket = ticketMapper.toEntity(createItemCommand);

        // 2. Call Domain Service (với business logic validation + persist)
        Ticket createdTicket = ticketDomainService.createTicket(ticket);

        // 3. Write-Through Cache: set Redis ngay sau khi DB thành công
        this.cacheTicket(createdTicket);

        log.info("Created & cached ticket ID: ", createdTicket.getId());

        // 4. Convert Entity → DTO
        return ticketMapper.toDTO(createdTicket);
    }

    // ========== CACHE METHODS ==========

    /**
     * Cache Ticket entity vào Redis
     */
    private void cacheTicket(Ticket ticket){
        try {
            log.info("Caching ticket ID: {}, name: {}", ticket.getId(), ticket.getName());
            if (ticket.getId() != null) {
                redisInfraService.setObject(TICKET_CACHE_PREFIX + ticket.getId(), ticket);
            }
        }catch (Exception e){
            // Cache failure không được block business flow
            log.warn("Failed to cache ticket ID: {}, error: {}", ticket.getId(), e.getMessage());
        }
    }

    /**
     * Xóa cache Ticket khi delete
     */
    private void evictTicketCache(Ticket ticket){
        try {
            redisInfraService.delete(TICKET_CACHE_PREFIX + ticket.getId());
        }catch (Exception e){
            log.warn("Failed to evict ticket ID: {}, error: {}", ticket.getId(), e.getMessage());
        }
    }
}
