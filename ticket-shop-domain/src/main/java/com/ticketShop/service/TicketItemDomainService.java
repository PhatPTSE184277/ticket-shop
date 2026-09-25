package com.ticketShop.service;


import com.ticketShop.model.entity.TicketItem;

public interface TicketItemDomainService {
    TicketItem getTicketItemById(Long id);
}
