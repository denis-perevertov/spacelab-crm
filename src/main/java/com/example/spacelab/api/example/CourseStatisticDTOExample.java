package com.example.spacelab.api.example;

import io.swagger.v3.oas.annotations.media.Schema;

public record CourseStatisticDTOExample(
        @Schema(defaultValue = "CourseName")
        String courseName,
        @Schema(defaultValue = "[2,0,0]")
        Object[] values
) {
}
