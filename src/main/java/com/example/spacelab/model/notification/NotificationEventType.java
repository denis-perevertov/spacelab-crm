package com.example.spacelab.model.notification;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NotificationEventType {
    COURSE_ASSIGNED("course.assigned"),
    TASK_CREATED("task.created"),
    TASK_REMOVED("task.removed"),
    STUDENT_ADDED("student.added"),
    STUDENT_REMOVED("student.removed"),
    LITERATURE_ADDED("literature.added"),
    LITERATURE_REMOVED("literature.removed"),
    LESSON_UPCOMING("lesson.upcoming"),
    LESSON_COMPLETED("lesson.completed"),
    LESSON_MISSED("lesson.missed");

    private final String code;

    public String getCode() {
        return code;
    }
}
