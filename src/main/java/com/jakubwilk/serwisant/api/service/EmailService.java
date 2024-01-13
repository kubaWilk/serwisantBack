package com.jakubwilk.serwisant.api.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.thymeleaf.context.Context;

public interface EmailService {

    @Async
    void sendEmail(String to, String subject, String templateName, Context context);
}
