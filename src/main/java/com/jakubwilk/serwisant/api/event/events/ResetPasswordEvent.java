package com.jakubwilk.serwisant.api.event.events;

import com.jakubwilk.serwisant.api.entity.PasswordResetToken;
import com.jakubwilk.serwisant.api.entity.User;
import org.springframework.context.ApplicationEvent;

public class ResetPasswordEvent extends ApplicationEvent {
    private final User user;
    private final PasswordResetToken token;
    public ResetPasswordEvent(Object source,
                              User user,
                              PasswordResetToken token) {
        super(source);
        this.user = user;
        this.token = token;
    }
}
