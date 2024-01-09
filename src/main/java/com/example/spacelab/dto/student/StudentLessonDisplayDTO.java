package com.example.spacelab.dto.student;

import java.util.List;

public record StudentLessonDisplayDTO(
        Long id,
        String name,
        Double hours,
        List<StudentTaskLessonDTO> openTasks,
        List<StudentTaskLessonDTO> nextTasks
) {
}
