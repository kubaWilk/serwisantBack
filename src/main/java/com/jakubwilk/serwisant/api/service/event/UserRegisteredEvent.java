package com.jakubwilk.serwisant.api.service.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserRegisteredEvent extends ApplicationEvent {
    private String message;

    public UserRegisteredEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
}
