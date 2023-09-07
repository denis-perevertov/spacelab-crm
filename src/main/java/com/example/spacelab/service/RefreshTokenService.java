package com.example.spacelab.service;

import com.example.spacelab.model.RefreshToken;

public interface RefreshTokenService {
    RefreshToken findByToken(String token);
    RefreshToken createRefreshToken(String username);
}
