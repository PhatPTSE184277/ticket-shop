package com.ticketShop.model.cache;

import com.ticketShop.model.entity.Ticket;
import lombok.Data;

@Data
public class TicketCache {
    private Long version;
    private Ticket ticket;

    public TicketCache withClone(Ticket ticket) {
        this.ticket = ticket;
        return this;
    }

    public TicketCache withVersion(Long version) {
        this.version = version;
        return this;
    }
}
