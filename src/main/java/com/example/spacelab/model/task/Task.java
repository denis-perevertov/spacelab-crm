package com.example.spacelab.model.task;

import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.literature.Literature;
import com.example.spacelab.model.student.Student;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="tasks")
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
    private String skillsDescription;
    private String taskDescription;

    @OneToMany
    private List<Task> subtasks;

    @ManyToMany
    private List<Literature> recommendedLiterature;

    @ManyToMany
    private List<Student> activeStudents;

    @Enumerated(value = EnumType.STRING)
    private TaskStatus status;

}
