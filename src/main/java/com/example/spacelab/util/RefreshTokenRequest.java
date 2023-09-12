package com.example.spacelab.util;

import io.swagger.v3.oas.annotations.media.Schema;

public record RefreshTokenRequest(@Schema(example = "[insert your token here]") String refresh_token) {
}
