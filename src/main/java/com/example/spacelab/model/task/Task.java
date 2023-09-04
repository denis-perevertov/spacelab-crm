package com.example.spacelab.model.task;

import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.lesson.LessonReportRow;
import com.example.spacelab.model.literature.Literature;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name="tasks")
@NoArgsConstructor
public class Task {

    /*
    TODO
    - время выполнения
     */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_task_id")
    private Task parentTask;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private TaskLevel level;

    private String completionTime;

    @Column(columnDefinition = "TEXT")
    private String skillsDescription;

    @Column(columnDefinition = "TEXT")
    private String taskDescription;

    @OneToMany
    private List<Task> subtasks;

    @ManyToMany
    private List<Literature> recommendedLiterature;

    @ManyToMany
    private List<Student> activeStudents;

    @ManyToMany
    private List<LessonReportRow> lessonReportRows;

    @Enumerated(value = EnumType.STRING)
    private TaskStatus status;

    public Task(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
