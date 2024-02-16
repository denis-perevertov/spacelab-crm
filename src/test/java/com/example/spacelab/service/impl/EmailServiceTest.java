package com.example.spacelab.service.impl;

import com.example.spacelab.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    void contextLoads() {
        assertThat(emailService).isNotNull();
    }

    @Test
    void sendForgotPasswordTest() {
        emailService.sendForgotPasswordMessage("regicuinte@gmail.com");
    }

    @Test
    void wrongEmailThrowsExceptionTest() {
        assertThrows(MailException.class, () -> emailService.sendForgotPasswordMessage("sldjflkdsjf@gmail.com"));
    }
}
