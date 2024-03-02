package com.example.spacelab.service.impl;

import com.example.spacelab.dto.notification.CourseEventNotificationDTO;
import com.example.spacelab.dto.notification.SimpleNotification;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.lesson.Lesson;
import com.example.spacelab.model.literature.Literature;
import com.example.spacelab.model.notification.CourseEventNotification;
import com.example.spacelab.model.notification.Notification;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.repository.notification.CourseEventNotificationRepository;
import com.example.spacelab.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final CourseEventNotificationRepository courseEventNotificationRepository;

    public void test() {
        simpMessagingTemplate.convertAndSend(
                "/topic/test",
                new CourseEventNotificationDTO(
                        1L,
                        "TEST",
                        2L,
                        "TEST",
                        "TEST",
                        "TEST"
                )
        );
    }

    @Override
    @Async
    public void sendAssignedToNewCourseNotification(Course course) {
        log.info("Sending assigned to new course notification...");
        simpMessagingTemplate.convertAndSend(
                "/topic/courses",
                new SimpleNotification(
                        course.getId(),
                        course.getName(),
                        "course.assigned",
                        course.getIcon()
                )
        );
    }

    @Override
    @Async
    public void sendTaskAddedToCourseNotification(Task task, Course course) {
        log.info("Sending task added to course notification...");
        simpMessagingTemplate.convertAndSend(
                String.format("/topic/courses/%s/tasks", course.getId()),
                new CourseEventNotificationDTO(
                        task.getId(),
                        task.getName(),
                        course.getId(),
                        course.getName(),
                        course.getIcon(),
                        "task.created"
                )
        );
    }

    @Override
    @Async
    public void sendTaskRemovedFromCourseNotification(Task task, Course course) {
        log.info("Sending task removed from course notification...");
        simpMessagingTemplate.convertAndSend(
                String.format("/topic/courses/%s/tasks", course.getId()),
                new CourseEventNotificationDTO(
                        task.getId(),
                        task.getName(),
                        course.getId(),
                        course.getName(),
                        course.getIcon(),
                        "task.removed"
                )
        );
    }

    @Override
    @Async
    public void sendStudentAddedToCourseNotification(Student student, Course course) {
        log.info("Sending student added to course notification...");
        simpMessagingTemplate.convertAndSend(
                String.format("/topic/courses/%s/students", course.getId()),
                new CourseEventNotificationDTO(
                        student.getId(),
                        student.getFullName(),
                        course.getId(),
                        course.getName(),
                        course.getIcon(),
                        "student.added"
                )
        );
    }

    @Override
    @Async
    public void sendStudentRemovedFromCourseNotififcation(Student student, Course course) {
        log.info("Sending student removed from course notification...");
        simpMessagingTemplate.convertAndSend(
                String.format("/topic/courses/%s/students", course.getId()),
                new CourseEventNotificationDTO(
                        student.getId(),
                        student.getFullName(),
                        course.getId(),
                        course.getName(),
                        course.getIcon(),
                        "student.removed"
                )
        );
    }

    @Override
    @Async
    public void sendNewLiteratureInCourseNotification(Literature literature, Course course) {
//        Notification notification = new CourseEventNotification()
//                .set
        log.info("Sending literature added to course notification...");
        simpMessagingTemplate.convertAndSend(
                String.format("/topic/courses/%s/literature", course.getId()),
                new CourseEventNotificationDTO(
                        literature.getId(),
                        literature.getName(),
                        course.getId(),
                        course.getName(),
                        course.getIcon(),
                        "literature.added"
                )
        );
    }

    @Override
    @Async
    public void sendUpcomingLessonNotification(Lesson lesson) {
        log.info("Sending upcoming lesson notification...");
        simpMessagingTemplate.convertAndSend(
                String.format("/topic/courses/%s/lessons", lesson.getCourse().getId()),
                new SimpleNotification(
                        lesson.getId(),
                        lesson.getDatetime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                        "lesson.upcoming",
                        lesson.getCourse().getIcon()
                )
        );
    }

    @Override
    public Notification saveNotification(Notification notification) {
        if(notification instanceof CourseEventNotification) {
            log.info("saving course event notification...");
            return courseEventNotificationRepository.save((CourseEventNotification) notification);
        }
        else return null;
    }
}
