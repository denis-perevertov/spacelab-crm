package com.example.spacelab.dto.student;

public record StudentTaskLessonDTO(
        Long id,
        int index,
        String name,
        String status,
        Integer percentOfCompletion
) {
}
