package com.example.spacelab.service;

import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.lesson.Lesson;
import com.example.spacelab.model.literature.Literature;
import com.example.spacelab.model.notification.Notification;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.student.StudentTask;
import com.example.spacelab.model.task.Task;

public interface NotificationService {

    void sendAssignedToNewCourseNotification(Course course);
    void sendTaskAddedToCourseNotification(Task task, Course course);
    void sendTaskRemovedFromCourseNotification(Task task, Course course);
    void sendStudentAddedToCourseNotification(Student student, Course course);
    void sendStudentRemovedFromCourseNotififcation(Student student, Course course);
    void sendNewLiteratureInCourseNotification(Literature literature, Course course);
    void sendUpcomingLessonNotification(Lesson lesson);

    Notification saveNotification(Notification notification);

}
