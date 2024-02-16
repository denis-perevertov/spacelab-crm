package com.example.spacelab.service;

import com.example.spacelab.model.RefreshToken;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
public interface RefreshTokenService {
    RefreshToken findByToken(String token);
    RefreshToken createRefreshToken(String username);
}
