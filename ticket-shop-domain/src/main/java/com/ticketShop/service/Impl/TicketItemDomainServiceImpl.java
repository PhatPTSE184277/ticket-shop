package com.ticketShop.service.Impl;

import com.ticketShop.model.entity.TicketItem;
import com.ticketShop.model.enums.TicketItemStatus;
import com.ticketShop.repository.TicketItemRepository;
import com.ticketShop.service.TicketItemDomainService;
import com.ticketShop.validator.TicketItemValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketItemDomainServiceImpl implements TicketItemDomainService {
    @Autowired
    private TicketItemRepository ticketItemRepository;

    @Autowired
    private TicketItemValidator ticketItemValidator;

    @Override
    public TicketItem getTicketItemById(Long id) {
        return ticketItemRepository.findById(id).orElse(null);
    }

    @Override
    public List<TicketItem> getTicketItemsByEventId(Long eventId) {
        return ticketItemRepository.findByEventId(eventId);
    }

    @Override
    public TicketItem createTicketItem(TicketItem ticketItem) {
        // 1. Validate TicketItem information
        ticketItemValidator.validateTicketItemCreation(ticketItem);

        // 2. Set audit fields for TicketItem
        ticketItem.setCreatedAt(LocalDateTime.now());
        ticketItem.setUpdatedAt(LocalDateTime.now());
        ticketItem.setStatus(TicketItemStatus.INACTIVE);

        return ticketItemRepository.save(ticketItem);
    }
}
