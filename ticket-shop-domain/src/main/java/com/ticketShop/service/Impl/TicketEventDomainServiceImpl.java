package com.ticketShop.service.Impl;

import com.ticketShop.model.entity.TicketEvent;
import com.ticketShop.model.entity.TicketItem;
import com.ticketShop.model.enums.TicketEventStatus;
import com.ticketShop.model.enums.TicketItemStatus;
import com.ticketShop.repository.TicketEventRepository;
import com.ticketShop.repository.TicketItemRepository;
import com.ticketShop.service.TicketEventDomainService;
import com.ticketShop.validator.TicketEventValidator;
import com.ticketShop.validator.TicketItemValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class TicketEventDomainServiceImpl implements TicketEventDomainService {
    @Autowired
    TicketEventRepository ticketEventRepository;

    @Autowired
    TicketItemRepository ticketItemRepository;

    @Autowired
    TicketEventValidator ticketEventValidator;

    @Autowired
    TicketItemValidator ticketItemValidator;

    @Override
    public List<TicketEvent> getAllActiveTicketEvents() {
        return ticketEventRepository.findAllActive();
    }

    @Override
    public TicketEvent createTicket(TicketEvent ticketEvent, TicketItem ticketItem) {
        // 1. Validate TicketEvent information
        ticketEventValidator.validateTicketEventCreation(ticketEvent);

        // 2. Set audit fields for TicketEvent
        ticketEvent.setCreatedAt(LocalDateTime.now());
        ticketEvent.setUpdatedAt(LocalDateTime.now());
        ticketEvent.setStatus(TicketEventStatus.INACTIVE);

        // 3. Persist TicketEvent
        TicketEvent savedTicketEvent = ticketEventRepository.save(ticketEvent);

        // 4. Create default TicketEvent
        ticketItem.setEventId(savedTicketEvent.getId());
        ticketItem.setCreatedAt(LocalDateTime.now());
        ticketItem.setUpdatedAt(LocalDateTime.now());
        ticketItem.setStatus(TicketItemStatus.INACTIVE);

        // Set defaults for nullable fields that DB requires NOT NULL
        if (ticketItem.getPriceFlash() == null){
            ticketItem.setPriceFlash(ticketItem.getPriceOriginal() != null ? ticketItem.getPriceOriginal().multiply(BigDecimal.valueOf(0.7)) : BigDecimal.ZERO);
        }
        if (ticketItem.getSaleStartTime() == null){
            ticketItem.setSaleStartTime(LocalDateTime.now());
        }
        if (ticketItem.getSaleEndTime() == null){
            ticketItem.setSaleEndTime(ticketItem.getSaleStartTime().plusDays(30));
        }

        // Validate TicketItem
        ticketItemValidator.validateTicketItemCreation(ticketItem);

        // 5. Persist TicketDetail
        ticketItemRepository.save(ticketItem);
        log.info("Created ticket event with ID: {} and default TicketItem", savedTicketEvent.getId());

        return savedTicketEvent;
    }
}
