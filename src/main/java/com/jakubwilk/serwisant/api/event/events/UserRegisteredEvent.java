package com.jakubwilk.serwisant.api.event.events;

import com.jakubwilk.serwisant.api.entity.jpa.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserRegisteredEvent extends ApplicationEvent {
    User user;
    String passsword;
    public UserRegisteredEvent(Object source, User theUser, String password) {
        super(source);
        this.user = theUser;
        this.passsword = password;
    }
}
