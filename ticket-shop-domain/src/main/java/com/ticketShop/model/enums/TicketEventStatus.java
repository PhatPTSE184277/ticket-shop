package com.ticketShop.model.enums;

import lombok.Getter;

@Getter
public enum TicketEventStatus {
    INACTIVE(0),
    ACTIVE(1),
    DELETED(2);

    private final int value;

    TicketEventStatus(int value) {
        this.value = value;
    }
}