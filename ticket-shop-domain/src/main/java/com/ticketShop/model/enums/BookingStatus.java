package com.ticketShop.model.enums;

import lombok.Getter;

@Getter
public enum BookingStatus {
    PENDING(0),
    CONFIRMED(1),
    CANCELLED(2);

    private final int value;

    BookingStatus(int value) {
        this.value = value;
    }
}