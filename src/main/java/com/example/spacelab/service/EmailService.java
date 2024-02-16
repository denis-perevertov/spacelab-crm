package com.example.spacelab.service;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
public interface EmailService {

    void sendForgotPasswordMessage(String receiver);
}
