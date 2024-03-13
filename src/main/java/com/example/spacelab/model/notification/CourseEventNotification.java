package com.example.spacelab.model.notification;

import com.example.spacelab.model.course.Course;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Entity
@Table(name = "notifications_course_events")
@Accessors(chain = true)
public class CourseEventNotification extends Notification {

    @ManyToOne
    private Course course;

    private Long entityId;
    private String entityName;

}
