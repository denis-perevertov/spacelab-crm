package com.example.spacelab.dto.student;

import java.util.List;

public record StudentTaskLessonListDTO(
        List<StudentTaskLessonDTO> unlockedTasks,
        List<StudentTaskLessonDTO> lockedTasks
) {
}
