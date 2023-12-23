package com.example.spacelab.model.lesson;

import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.course.Course;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="lessons")
@NoArgsConstructor
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

    private Boolean startsAutomatically = false;

    @Enumerated(value = EnumType.STRING)
    private LessonStatus status;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="lesson_report_id")
    private LessonReport lessonReport;

    public Lesson(Long id, LocalDateTime datetime) {
        this.id = id;
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", datetime=" + datetime +
                ", startsAutomatically=" + startsAutomatically +
                ", status=" + status +
                '}';
    }
}
