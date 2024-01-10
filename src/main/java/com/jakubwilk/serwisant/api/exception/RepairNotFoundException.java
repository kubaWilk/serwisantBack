package com.jakubwilk.serwisant.api.exception;

public class RepairNotFoundException extends RuntimeException{
    public RepairNotFoundException(String message) {
        super(message);
    }

    public RepairNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepairNotFoundException(Throwable cause) {
        super(cause);
    }
}
