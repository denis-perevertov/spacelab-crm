package com.example.spacelab.util;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthRequest(@Schema(example = "admin@gmail.com") String username,
                          @Schema(example = "admin") String password) {
}
