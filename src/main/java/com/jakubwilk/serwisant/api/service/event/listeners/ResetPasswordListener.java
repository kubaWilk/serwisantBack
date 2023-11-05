package com.jakubwilk.serwisant.api.service.event.listeners;

import com.jakubwilk.serwisant.api.service.event.events.ResetPasswordEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ResetPasswordListener implements ApplicationListener<ResetPasswordEvent> {
    @Override
    public void onApplicationEvent(ResetPasswordEvent event) {
        System.out.println("Generated a user reset password token and sent an e-mail (NOT YET IMPLEMENTED!)");
    }
}
