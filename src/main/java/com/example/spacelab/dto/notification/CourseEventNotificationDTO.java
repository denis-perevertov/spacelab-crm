package com.example.spacelab.dto.notification;

public record CourseEventNotificationDTO(
        Long entityId,
        String entityName,
        Long courseId,
        String courseName,
        String courseIcon,
        String eventType
) {
}
