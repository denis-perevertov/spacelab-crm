package com.example.spacelab.util;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthRequest(
        @Schema(example = "admin@gmail.com") String username,
        @Schema(example = "admin") String password
) {
    public AuthRequest {
        username = (username != null) ? username.trim() : null;
        password = (password != null) ? password.trim() : null;
    }
}

