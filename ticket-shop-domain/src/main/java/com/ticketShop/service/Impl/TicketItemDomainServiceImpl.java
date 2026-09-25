package com.ticketShop.service.Impl;

import com.ticketShop.model.entity.TicketItem;
import com.ticketShop.repository.TicketItemRepository;
import com.ticketShop.service.TicketItemDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketItemDomainServiceImpl implements TicketItemDomainService {
    @Autowired
    private TicketItemRepository ticketItemRepository;

    @Override
    public TicketItem getTicketItemById(Long id) {
        return ticketItemRepository.findById(id).orElse(null);
    }
}
