package com.example.spacelab.dto.course;

import com.example.spacelab.dto.student.StudentTaskDTO;

import java.util.List;

public record StudentCourseTaskInfoDTO(
        Long id,
        String name,
        String icon,
        List<StudentTaskDTO> tasks
) {
}
