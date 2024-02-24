package com.example.spacelab.controller.websocket;

import com.example.spacelab.service.NotificationService;
import com.example.spacelab.service.impl.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebsocketController {

    /*
        /topic/tasks - for new tasks in the course ?
        /topic/literature - for new literature in the course ?
        /topic/lessons - for upcoming lessons ?
        /topic/courses - for being assigned to a new course
        /topic/students - for students being registered in a course
        /topic/students/tasks - for students getting tasks ready

     */

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationServiceImpl notificationService;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting (HelloMessage message) throws InterruptedException {
        log.info("MESSAGE MAPPING");
        Thread.sleep(1000);
        return new Greeting("Hello, " + message.name() + "!");
    }

    @MessageMapping("/test")
    public void test() {
        notificationService.test();
    }

    @GetMapping("/test")
    public ResponseEntity<?> test2() {
        notificationService.test();
        return ResponseEntity.ok().build();
    }

//    @MessageMapping("/tasks")
//    @SendTo("/topic/tasks/{courseId}")
//    public Notification newTaskInCourseNotification(@DestinationVariable Long courseId) {
//        log.info("Sending notification for new task created in course");
//        return null;
//    }

}

record HelloMessage(
        String name
) {}

record Greeting(
        String message
) {}

record Notification (
        String title,
        String subtitle,
        String avatarImg,
        String link
) {}
