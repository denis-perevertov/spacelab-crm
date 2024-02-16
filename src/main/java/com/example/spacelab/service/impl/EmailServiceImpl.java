package com.example.spacelab.service.impl;

import com.example.spacelab.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String hostname;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${application.email.forgot-password-code-length}")
    private int codeLength;

    private final MailSender mailSender;

    @Override
    public void sendForgotPasswordMessage(String receiver) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(hostname);
        message.setTo(receiver);
        message.setSubject("SpaceLab LMS - Password Recovery Code");
        message.setText(generateCode());
        log.info("Created message, sending to {}...", receiver);
        mailSender.send(message);
    }

    // todo save code for specific email

    private String generateCode() {
        Random random = new Random();
        final String symbols = "ABCDEFGHIJKLMNOPQRSTUVXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            sb.append(symbols.charAt(random.nextInt(symbols.length()-1)));
        }
        return sb.toString();
    }

}
