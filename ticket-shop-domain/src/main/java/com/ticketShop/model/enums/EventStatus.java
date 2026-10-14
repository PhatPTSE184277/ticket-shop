package com.ticketShop.model.enums;

import lombok.Getter;

@Getter
public enum EventStatus {
    INACTIVE(0),
    ACTIVE(1),
    DELETED(2);

    private final int value;

    EventStatus(int value) {
        this.value = value;
    }
}