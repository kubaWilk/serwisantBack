package com.jakubwilk.serwisant.api.service.event.listeners;

import com.jakubwilk.serwisant.api.service.event.events.UserRegisteredEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class UserRegisteredEventListener implements ApplicationListener<UserRegisteredEvent>{
    @Override
    public void onApplicationEvent(UserRegisteredEvent event) {
        System.out.println("Sent an email to user(IMPLEMENT E-MAIL SENDING): " + event.getSource());
    }
}
