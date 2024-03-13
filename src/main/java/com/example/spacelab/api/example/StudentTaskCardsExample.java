package com.example.spacelab.api.example;

import com.example.spacelab.dto.task.StudentTaskLinkDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public record StudentTaskCardsExample(
        StudentTaskLinkDTO lastCompleted,
        @Schema(defaultValue = "123")
        long completedAmount
) {
}
