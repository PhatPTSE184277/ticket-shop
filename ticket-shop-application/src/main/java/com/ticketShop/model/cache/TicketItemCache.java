package com.ticketShop.model.cache;

import com.ticketShop.model.entity.TicketItem;
import lombok.Data;

@Data
public class TicketItemCache {
    private Long version;
    private TicketItem ticketItem;

    public TicketItemCache withClone(TicketItem ticketItem) {
        this.ticketItem = ticketItem;
        return this;
    }

    public TicketItemCache withVersion(Long version) {
        this.version = version;
        return this;
    }
}
