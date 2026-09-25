package com.ticketShop.model.enums;

import lombok.Getter;

@Getter
public enum TicketItemStatus {

    INACTIVE(0),
    ACTIVE(1),
    SOLD_OUT(2),
    DELETED(3);

    private final int value;

    TicketItemStatus(int value) {
        this.value = value;
    }
}