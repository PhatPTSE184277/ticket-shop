package com.ticketShop.model.enums;

import lombok.Getter;

@Getter
public enum OutboxEventStatus {
    PENDING(0),
    PUBLISHED(1);

    private final int value;

    OutboxEventStatus(int value) {
        this.value = value;
    }
}