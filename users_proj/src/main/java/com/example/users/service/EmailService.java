package com.example.users.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String from;

    public void sendVerificationCode(String to, String code) {
        // Without an SMTP account (MAIL_USERNAME), the code goes to this console instead.
        if (from.isBlank()) {
            log.warn("MAIL_USERNAME is not set, so no email was sent. Verification code for {}: {}", to, code);
            return;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Your CarHub verification code");
        message.setText("Your CarHub verification code is " + code + ". It expires in 15 minutes.");
        mailSender.send(message);
    }
}
