package com.ticketShop.model.enums;

import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum PaymentStatus {
    INIT(0),
    IN_PROGRESS(1),
    SUCCESS(2),
    FAILED(3);

    private final int value;

    private static final Map<Integer, PaymentStatus> BY_VALUE = new HashMap<>();

    static {
        for (PaymentStatus status : values()) {
            BY_VALUE.put(status.value, status);
        }
    }

    PaymentStatus(int value) {
        this.value = value;
    }

    public static PaymentStatus fromValue(int value) {
        PaymentStatus status = BY_VALUE.get(value);
        if (status == null) {
            throw new IllegalArgumentException("Unknown PaymentStatus value: " + value);
        }
        return status;
    }
}