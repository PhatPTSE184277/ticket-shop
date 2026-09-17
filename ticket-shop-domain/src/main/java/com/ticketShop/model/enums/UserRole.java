package com.ticketShop.model.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    CUSTOMER("CUSTOMER"),
    STAFF("STAFF"),
    ADMIN("ADMIN");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }
}