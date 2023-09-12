package com.example.spacelab.util;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthResponse(@Schema(example = "[access token here]") String access_token,
                           @Schema(example = "[refresh token here]") String refresh_token) {
}
