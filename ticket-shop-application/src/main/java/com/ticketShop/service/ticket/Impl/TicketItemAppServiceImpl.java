package com.ticketShop.service.ticket.Impl;

import com.ticketShop.cache.RedisInfraService;
import com.ticketShop.exception.enums.ResultCode;
import com.ticketShop.mapper.TicketItemMapper;
import com.ticketShop.model.cache.TicketItemCache;
import com.ticketShop.model.command.CreateTicketItemCommand;
import com.ticketShop.model.dto.TicketItemDTO;
import com.ticketShop.model.entity.TicketEvent;
import com.ticketShop.model.entity.TicketItem;
import com.ticketShop.service.TicketItemDomainService;
import com.ticketShop.service.ticket.TicketItemAppService;
import com.ticketShop.service.ticket.cache.TicketItemCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ticketShop.exception.ServiceException;

import java.util.List;

@Service
@Slf4j
public class TicketItemAppServiceImpl implements TicketItemAppService {
    private static final String TICKET_ITEM_CACHE_PREFIX = "PRO_TICKET:ITEM:";

    @Autowired
    private TicketItemCacheService ticketItemCacheService;

    @Autowired
    private TicketItemMapper ticketItemMapper;

    @Autowired
    private TicketItemDomainService ticketItemDomainService;

    @Autowired
    private RedisInfraService redisInfraService;

    @Override
    public TicketItemDTO getTicketItemById(Long id, Long version) {
        log.info("Implement Application : {}, {}: ", id, version);
        TicketItemCache ticketItemCache = ticketItemCacheService.getTicketItem(id, version);
        if (ticketItemCache == null || ticketItemCache.getTicketItem() == null) {
            log.warn("Ticket detail not found or lock acquire timeout for id: {}", id);
            throw new ServiceException(ResultCode.TICKET_ITEM_NOT_EXIST);
        }
        // mapper to DTO
        TicketItemDTO ticketItemDTO = ticketItemMapper.toDTO(ticketItemCache.getTicketItem());
        ticketItemDTO.setVersion(ticketItemCache.getVersion());
        return ticketItemDTO;
    }

    @Override
    public List<TicketItemDTO> getTicketItemsByEventId(Long eventId) {
        List<TicketItemCache> ticketItemCaches = ticketItemCacheService.getTicketItemsByEvent(eventId
        );

        if (ticketItemCaches == null  || ticketItemCaches.isEmpty()) {
            throw new ServiceException(ResultCode.EVENT_NOT_EXIST);
        }
        return ticketItemCaches.stream()
                .map(TicketItemCache::getTicketItem)
                .map(ticketItemMapper::toDTO)
                .toList();
    }

    @Override
    public TicketItemDTO createTicketItem(CreateTicketItemCommand createItemCommand) {
        //1.Convert Request -> Entity (via Mapper)
        TicketItem ticketItem = ticketItemMapper.toEntity(createItemCommand);

        // 2. Call Domain Service (với business logic validation + persist)
        TicketItem createdTicket = ticketItemDomainService.createTicketItem(ticketItem);

        // 3. Write-Through Cache: set Redis ngay sau khi DB thành công
        this.cacheTicketItem(createdTicket);

        log.info("Created & cached ticket item ID: ", createdTicket.getId());

        // 4. Convert Entity → DTO
        return ticketItemMapper.toDTO(createdTicket);
    }

    // ========== CACHE METHODS ==========

    /**
     * Cache Ticket Item entity vào Redis
     */
    private void cacheTicketItem(TicketItem ticketItem){
        try {
            log.info("Caching ticket item ID: {}, name: {}", ticketItem.getId(), ticketItem.getName());
            if (ticketItem.getId() != null) {
                redisInfraService.setObject(TICKET_ITEM_CACHE_PREFIX + ticketItem.getId(), ticketItem);
            }
        }catch (Exception e){
            // Cache failure không được block business flow
            log.warn("Failed to cache ticket item ID: {}, error: {}", ticketItem.getId(), e.getMessage());
        }
    }

    /**
     * Xóa cache TicketItem khi delete
     */
    private void evictTicketItemCache(TicketItem ticketItem){
        try {
            redisInfraService.delete(TICKET_ITEM_CACHE_PREFIX + ticketItem.getId());
        }catch (Exception e){
            log.warn("Failed to evict ticket item ID: {}, error: {}", ticketItem.getId(), e.getMessage());
        }
    }
}
