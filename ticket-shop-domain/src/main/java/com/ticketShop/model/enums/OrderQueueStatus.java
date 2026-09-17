package com.ticketShop.model.enums;

import lombok.Getter;

@Getter
public enum OrderQueueStatus {
    PENDING(0),
    SUCCESS(1),
    FAILED(2);

    private final int value;

    OrderQueueStatus(int value) {
        this.value = value;
    }
}