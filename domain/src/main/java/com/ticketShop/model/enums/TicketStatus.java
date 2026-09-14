package com.ticketShop.model.enums;

import lombok.Getter;

@Getter
public enum TicketStatus {
    INACTIVE(0),
    ACTIVE(1),
    DELETED(2);

    private final int value;

    TicketStatus(int value) {
        this.value = value;
    }
}