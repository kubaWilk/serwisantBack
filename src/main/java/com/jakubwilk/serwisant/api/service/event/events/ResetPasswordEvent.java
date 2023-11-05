package com.jakubwilk.serwisant.api.service.event.events;

import org.springframework.context.ApplicationEvent;

public class ResetPasswordEvent extends ApplicationEvent {
    private final String message;

    public ResetPasswordEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
}
