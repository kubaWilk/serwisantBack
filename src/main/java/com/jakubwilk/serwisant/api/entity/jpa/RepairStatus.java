package com.jakubwilk.serwisant.api.entity.jpa;

import lombok.Getter;

@Getter
public enum RepairStatus {
    OPEN("Open"),
    WAITING_FOR_CUSTOMER("Waiting for Customer"),
    WAITING_FOR_SUPPLIER("Waiting for Supplier"),
    CANCELED("Canceled"),
    CLOSED("Closed");

    private final String displayValue;
    RepairStatus(String displayValue) {
        this.displayValue = displayValue;
    }
}