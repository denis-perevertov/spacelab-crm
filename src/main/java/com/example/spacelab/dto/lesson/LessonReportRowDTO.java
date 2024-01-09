package com.example.spacelab.dto.lesson;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

public record LessonReportRowDTO (
        Long id,
        Long studentId,
        String studentName,
        Boolean wasPresent,
        String currentTasks,
        Double hours,
        String hoursNote,
        String comment,
        Integer rating
) {
}
