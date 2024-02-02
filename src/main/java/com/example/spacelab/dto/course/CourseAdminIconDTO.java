package com.example.spacelab.dto.course;

public record CourseAdminIconDTO(
        Long id,
        String name,
        String icon,
        Long mentorId,
        String mentorName,
        Long managerId,
        String managerName
) {
}
