package com.jakubwilk.serwisant.api.service.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class UserEventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishUserEvent(final String message){
        UserRegisteredEvent userRegisteredEvent = new UserRegisteredEvent(this, message);
        applicationEventPublisher.publishEvent(userRegisteredEvent);
    }
}
