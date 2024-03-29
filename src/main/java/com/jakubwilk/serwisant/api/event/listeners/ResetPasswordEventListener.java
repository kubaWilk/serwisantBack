package com.jakubwilk.serwisant.api.event.listeners;

import com.jakubwilk.serwisant.api.event.events.ResetPasswordEvent;
import com.jakubwilk.serwisant.api.service.EmailService;
import com.jakubwilk.serwisant.api.utils.ApplicationProperties;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

@Component
@AllArgsConstructor
public class ResetPasswordEventListener implements ApplicationListener<ResetPasswordEvent> {
    private final EmailService emailService;

    private final ApplicationProperties applicationProperties;

    @Override
    public void onApplicationEvent(ResetPasswordEvent event) {
        String to = event.getUser().getEmail();
        String subject = "Zresetowano hasło dla twojego konta";
        String resetUrl =
                applicationProperties.getFrontUrl()
                        + "/reset-password/" + event.getToken().getToken();

        Context context = new Context();
        context.setVariable("resetPasswordLink", resetUrl);

        emailService.sendEmail(to, subject, "ResetPasswordEmail", context);
    }
}
