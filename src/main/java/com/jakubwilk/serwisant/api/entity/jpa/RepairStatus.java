package com.jakubwilk.serwisant.api.entity.jpa;

import lombok.Getter;
import lombok.ToString;

@Getter
public enum RepairStatus {
    OPEN("Otwarta"),
    WAITING_FOR_CUSTOMER("Oczekuje na klienta"),
    WAITING_FOR_SUPPLIER("Oczekuje na dostawcę"),
    CANCELED("Anulowana"),
    CLOSED("Zamknięta");

    private final String displayValue;
    RepairStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    @Override
    public String toString(){
        return displayValue;
    }
}