package com.example.spacelab.api.example;

import io.swagger.v3.oas.annotations.media.Schema;

public record StudentRatingStatisticExample(
        @Schema(defaultValue = "84.6")
        double total,
        @Schema(defaultValue = "99.1")
        double recent
) {
}
