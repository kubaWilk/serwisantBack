package com.jakubwilk.serwisant.api.event.listeners;

import com.jakubwilk.serwisant.api.event.events.RepairCreatedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class RepairCreatedEventListener implements ApplicationListener<RepairCreatedEvent> {
    @Override
    public void onApplicationEvent(RepairCreatedEvent event) {
        System.out.println("Implement RepairCreatedEvent!");
    }
}
