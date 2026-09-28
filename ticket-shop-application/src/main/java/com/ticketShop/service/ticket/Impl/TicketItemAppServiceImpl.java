package com.ticketShop.service.ticket.Impl;

import com.ticketShop.exception.enums.ResultCode;
import com.ticketShop.mapper.TicketItemMapper;
import com.ticketShop.model.cache.TicketItemCache;
import com.ticketShop.model.dto.response.TicketItemResponse;
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
    @Autowired
    private TicketItemCacheService ticketItemCacheService;

    @Autowired
    private TicketItemMapper ticketItemMapper;

    @Override
    public TicketItemResponse getTicketItemById(Long id, Long version) {
        log.info("Implement Application : {}, {}: ", id, version);
        TicketItemCache ticketItemCache = ticketItemCacheService.getTicketItem(id, version);
        if (ticketItemCache == null || ticketItemCache.getTicketItem() == null) {
            log.warn("Ticket detail not found or lock acquire timeout for id: {}", id);
            throw new ServiceException(ResultCode.TICKET_ITEM_NOT_EXIST);
        }
        // mapper to DTO
        TicketItemResponse ticketItemResponse = ticketItemMapper.toResponse(ticketItemCache.getTicketItem());
        ticketItemResponse.setVersion(ticketItemCache.getVersion());
        return ticketItemResponse;
    }

    @Override
    public List<TicketItemResponse> getTicketItemsByEventId(Long eventId) {
        List<TicketItemCache> ticketItemCaches = ticketItemCacheService.getTicketItemsByEvent(eventId
        );

        if (ticketItemCaches == null  || ticketItemCaches.isEmpty()) {
            throw new ServiceException(ResultCode.EVENT_NOT_EXIST);
        }
        return ticketItemCaches.stream()
                .map(TicketItemCache::getTicketItem)
                .map(ticketItemMapper::toResponse)
                .toList();
    }
}
