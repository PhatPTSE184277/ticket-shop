package com.ticketShop.model.enums;

import lombok.Getter;

@Getter
public enum TicketStatus {

    INACTIVE(0),
    ACTIVE(1),
    SOLD_OUT(2),
    DELETED(3);

    private final int value;

    TicketStatus(int value) {
        this.value = value;
    }
}