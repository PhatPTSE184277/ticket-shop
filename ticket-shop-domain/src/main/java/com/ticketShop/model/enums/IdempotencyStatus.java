package com.ticketShop.model.enums;

import lombok.Getter;

@Getter
public enum IdempotencyStatus {

    PENDING(0),
    SUCCESS(1),
    FAILED(2);

    private final int value;

    IdempotencyStatus(int value) {
        this.value = value;
    }
}