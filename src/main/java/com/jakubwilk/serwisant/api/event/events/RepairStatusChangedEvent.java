package com.jakubwilk.serwisant.api.event.events;

import com.jakubwilk.serwisant.api.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RepairStatusChangedEvent extends ApplicationEvent {
    private final User user;
    public RepairStatusChangedEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
