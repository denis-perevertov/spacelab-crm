package com.example.spacelab.dto.statistics;

import com.example.spacelab.model.student.StudentAccountStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record StudentRatingTableDTO(
        Long id,
        String name,
        Long courseId,
        String courseName,
        String courseIcon,
        Integer rating,
        Double averageLearningTime,
        @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
        LocalDateTime registrationDate,
        Integer learningDuration,
        StudentAccountStatus status
) {
}
