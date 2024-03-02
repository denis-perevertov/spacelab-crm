package com.example.spacelab.api.example;

import io.swagger.v3.oas.annotations.media.Schema;

public record StudentLearningTimeExample(
        TimeTotalExample total,
        TimeTotalExample recent
) {
    public record TimeTotalExample (
            @Schema(defaultValue = "1000.0")
            Double hours,
            @Schema(defaultValue = "30")
            Integer minutes
    ) {}
}
