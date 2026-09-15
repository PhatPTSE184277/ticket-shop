package com.ticketShop.model.enums;

import lombok.Getter;

@Getter
public enum UserStatus {
    INACTIVE(0),
    ACTIVE(1),
    BLOCKED(2);

    private final int value;

    UserStatus(int value) {
        this.value = value;
    }
}