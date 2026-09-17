package com.ticketShop.service.ticket.Impl;

import com.ticketShop.mapper.TicketDetailMapper;
import com.ticketShop.model.TicketDetailDTO;
import com.ticketShop.model.cache.TicketDetailCache;
import com.ticketShop.service.ticket.TicketDetailAppService;
import com.ticketShop.service.ticket.cache.TicketDetailCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class TicketDetailAppServiceImpl implements TicketDetailAppService {
    @Autowired
    private TicketDetailCacheService  ticketDetailCacheService;

    @Override
    public TicketDetailDTO getTicketDetailById(Long id, Long version) {
        log.info("Implement Application : {}, {}: ", id, version);
        TicketDetailCache ticketDetailCache = ticketDetailCacheService.getTicketDetail(id, version);
        if (ticketDetailCache == null || ticketDetailCache.getTicketDetail() == null) {
            log.warn("Ticket detail not found or lock acquire timeout for id: {}", id);
            return null;
        }
        // mapper to DTO
        TicketDetailDTO ticketDetailDTO = TicketDetailMapper.mapperTOTicketDetailDTO(ticketDetailCache.getTicketDetail());
        ticketDetailDTO.setVersion(ticketDetailCache.getVersion());
        return ticketDetailDTO;
    }
}
