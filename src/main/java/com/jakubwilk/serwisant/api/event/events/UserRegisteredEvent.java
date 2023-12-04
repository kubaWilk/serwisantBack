package com.jakubwilk.serwisant.api.event.events;

import com.jakubwilk.serwisant.api.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserRegisteredEvent extends ApplicationEvent {
    User user;
    public UserRegisteredEvent(Object source, User theUser) {
        super(source);
        this.user = theUser;
    }
}
