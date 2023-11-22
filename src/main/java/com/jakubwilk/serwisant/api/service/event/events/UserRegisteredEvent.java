package com.jakubwilk.serwisant.api.service.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserRegisteredEvent extends ApplicationEvent {
    private final String message;

    public UserRegisteredEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
}