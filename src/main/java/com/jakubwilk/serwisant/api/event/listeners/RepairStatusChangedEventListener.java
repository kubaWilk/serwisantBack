package com.jakubwilk.serwisant.api.event.listeners;

import com.jakubwilk.serwisant.api.event.events.RepairStatusChangedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class RepairStatusChangedEventListener implements ApplicationListener<RepairStatusChangedEvent> {
    @Override
    public void onApplicationEvent(RepairStatusChangedEvent event) {
        System.out.println("Implement RepairStatusChangedEvent!");
    }
}
