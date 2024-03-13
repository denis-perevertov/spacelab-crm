package com.example.spacelab.api.example;

import com.example.spacelab.dto.lesson.LessonLinkDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public record StudentLessonCardsExample(
        LessonLinkDTO lastVisitedLesson,
        LessonLinkDTO nextLesson,
        @Schema(defaultValue = "123")
        long visitedAmount,
        @Schema(defaultValue = "2")
        long skippedAmount
) {
}
