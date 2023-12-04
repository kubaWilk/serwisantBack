package com.jakubwilk.serwisant.api.event.listeners;

import com.jakubwilk.serwisant.api.event.events.UserRegisteredEvent;
import com.jakubwilk.serwisant.api.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

@Component
@AllArgsConstructor
public class UserRegisteredEventListener implements ApplicationListener<UserRegisteredEvent>{
    private final EmailService emailService;
    @Override
    public void onApplicationEvent(UserRegisteredEvent event) {
        String to = event.getUser().getEmail();
        String subject = "Witaj w serwisie";

        Context context = new Context();
        context.setVariable("bodyText", "Testowy e-mail");

        emailService.sendEmail(to, subject,
                "UserRegisteredEmail", context);
    }
}
