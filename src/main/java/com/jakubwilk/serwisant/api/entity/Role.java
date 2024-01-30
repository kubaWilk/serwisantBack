package com.jakubwilk.serwisant.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
public enum Role {
    ROLE_CUSTOMER("ROLE_CUSTOMER"),
    ROLE_EMPLOYEE("ROLE_EMPLOYEE"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String displayValue;

    Role(String displayValue) {
        this.displayValue = displayValue;
    }
}
