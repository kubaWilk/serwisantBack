package com.jakubwilk.serwisant.api.exception;

public class RepairAlreadyClosedException extends RuntimeException{
    public RepairAlreadyClosedException(String message) {
        super(message);
    }

    public RepairAlreadyClosedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepairAlreadyClosedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
