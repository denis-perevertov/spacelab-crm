package com.example.spacelab.dto.student;

public record StudentTaskLessonDTO(
        Long id,
        String index,
        String name,
        String status,
        Integer percentOfCompletion
) {
}
