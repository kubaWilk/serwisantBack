package com.jakubwilk.serwisant.api.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
@AllArgsConstructor
public class EmailService{
    private JavaMailSender mailSender;
    private TemplateEngine templateEngine;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    @Async
    public void sendEmail(String to, String subject, String templateName, Context context){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);

            String htmlContent = templateEngine.process(templateName, context);
            helper.setText(htmlContent, true);
            logger.info("Sending an email message to: " + to);
            mailSender.send(message);
        }catch(MessagingException ex){
            logger.error(ex.getMessage(), ex);
        }
    }
}
