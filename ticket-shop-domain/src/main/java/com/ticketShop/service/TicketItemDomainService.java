package com.ticketShop.service;


import com.ticketShop.model.entity.TicketItem;

import java.util.List;

public interface TicketItemDomainService {
    TicketItem getTicketItemById(Long id);

    List<TicketItem> getTicketItemsByEventId(Long eventId);

    TicketItem createTicketItem(TicketItem ticketItem);
}
