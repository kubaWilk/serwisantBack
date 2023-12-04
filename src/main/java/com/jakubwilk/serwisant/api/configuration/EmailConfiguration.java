package com.jakubwilk.serwisant.api.configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfiguration {

    @Value("${spring.mail.protocol}")
    private String mailProtocol;

    @Value("${spring.mail.port}")
    private Integer mailServerPort;

    @Value("${spring.mail.host}")
    private String mailServerAddress;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${spring.mail.password}")
    private String mailPassword;

    @Value("${spring.mail.smtp.auth}")
    private boolean mailSmtpAuth;

    @Value("${spring.mail.smtp.starttls.enable}")
    private boolean mailSmtpStarttlsEnable;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailServerAddress);
        mailSender.setPort(mailServerPort);
        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);
        mailSender.setProtocol(mailProtocol);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", mailSmtpAuth);
        props.put("mail.smtp.starttls.enable", mailSmtpStarttlsEnable);

        return mailSender;
    }
}
