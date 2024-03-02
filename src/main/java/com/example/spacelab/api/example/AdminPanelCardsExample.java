package com.example.spacelab.api.example;

import io.swagger.v3.oas.annotations.media.Schema;

public record AdminPanelCardsExample(
        @Schema(defaultValue = "50")
        Long activeStudents,
        @Schema(defaultValue = "15")
        Long activeCourses,
        @Schema(defaultValue = "200")
        Long activeTasks,
        @Schema(defaultValue = "4999")
        Long completedLessons,
        @Schema(defaultValue = "1")
        Long hiredStudents,
        @Schema(defaultValue = "1000")
        Long expelledStudents,
        @Schema(defaultValue = "1500")
        Long totalStudents
) {
}
