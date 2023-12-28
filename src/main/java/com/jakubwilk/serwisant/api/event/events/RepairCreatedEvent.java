package com.jakubwilk.serwisant.api.event.events;

import com.jakubwilk.serwisant.api.entity.jpa.Repair;
import com.jakubwilk.serwisant.api.entity.jpa.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RepairCreatedEvent extends ApplicationEvent {
    private final User user;
    private final Repair repair;

    public RepairCreatedEvent(Object source, User user, Repair repair) {
        super(source);
        this.user = user;
        this.repair = repair;
    }
}
