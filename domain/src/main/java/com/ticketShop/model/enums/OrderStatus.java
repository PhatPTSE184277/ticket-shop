package com.ticketShop.model.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING(0),
    SUCCESS(1),
    CANCELLED(2),
    EXPIRED(3),
    REFUNDED(4);

    private final int value;

    OrderStatus(int value) {
        this.value = value;
    }
}