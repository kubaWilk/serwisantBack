package com.jakubwilk.serwisant.api.service.event;

import com.jakubwilk.serwisant.api.entity.User;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserRegisteredEventListener implements ApplicationListener<UserRegisteredEvent>{
    @Override
    public void onApplicationEvent(UserRegisteredEvent event) {
        //TODO: create proper handle for event
    }
}
