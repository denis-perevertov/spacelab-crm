package com.example.spacelab.dto.lesson;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.ZonedDateTime;

public record LessonReportRowDTO (
        Long id,
        Long lessonId,
        @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
        ZonedDateTime datetime,
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
