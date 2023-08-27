package com.example.spacelab.model.lesson;

import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.course.Course;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @DateTimeFormat(pattern = "MM/dd/yyyy - HH:mm")
    private LocalDateTime datetime;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private String link;

    private Boolean startsAutomatically;

    @Enumerated(value = EnumType.STRING)
    private LessonStatus status;

    @OneToOne
    private LessonReport lessonReport;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Admin mentor;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin manager;
}
