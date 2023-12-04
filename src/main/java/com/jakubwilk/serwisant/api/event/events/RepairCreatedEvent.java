package com.jakubwilk.serwisant.api.event.events;

import com.jakubwilk.serwisant.api.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RepairCreatedEvent extends ApplicationEvent {
    private final User user;

    public RepairCreatedEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
