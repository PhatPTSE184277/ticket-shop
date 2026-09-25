package com.ticketShop.service.ticket.Impl;

import com.ticketShop.exception.enums.ResultCode;
import com.ticketShop.mapper.TicketItemMapper;
import com.ticketShop.model.cache.TicketItemCache;
import com.ticketShop.model.dto.response.TicketItemResponse;
import com.ticketShop.service.ticket.TicketItemAppService;
import com.ticketShop.service.ticket.cache.TicketDetailCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ticketShop.exception.ServiceException;

@Service
@Slf4j
public class TicketItemAppServiceImpl implements TicketItemAppService {
    @Autowired
    private TicketDetailCacheService  ticketDetailCacheService;

    @Autowired
    private TicketItemMapper ticketItemMapper;

    @Override
    public TicketItemResponse getTicketItemById(Long id, Long version) {
        log.info("Implement Application : {}, {}: ", id, version);
        TicketItemCache ticketDetailCache = ticketDetailCacheService.getTicketDetail(id, version);
        if (ticketDetailCache == null || ticketDetailCache.getTicketItem() == null) {
            log.warn("Ticket detail not found or lock acquire timeout for id: {}", id);
            throw new ServiceException(ResultCode.TICKET_DETAIL_NOT_EXIST);
        }
        // mapper to DTO
        TicketItemResponse ticketItemResponse = ticketItemMapper.toResponse(ticketDetailCache.getTicketItem());
        ticketItemResponse.setVersion(ticketDetailCache.getVersion());
        return ticketItemResponse;
    }
}
