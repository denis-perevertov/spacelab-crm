package com.example.spacelab.dto.task;

import com.example.spacelab.model.student.StudentTaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.ZonedDateTime;

public record StudentTaskCardDTO(
        Long id,
        StudentTaskStatus status,
        @JsonFormat(pattern = "dd.MM.yyyy")
        ZonedDateTime startDate,
        @JsonFormat(pattern = "dd.MM.yyyy")
        ZonedDateTime endDate,
        TaskInfoDTO taskReference,
        StudentTaskLinkDTO parentStudentTask,
        String taskListId,
        String projectId
) {
}
