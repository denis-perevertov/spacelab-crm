package com.example.spacelab.repository.notification;

import com.example.spacelab.model.notification.CourseEventNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseEventNotificationRepository extends JpaRepository<CourseEventNotification, Long> {
}
