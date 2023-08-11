package com.example.spacelab.model;

import com.example.spacelab.util.TaskLevel;
import com.example.spacelab.util.TaskStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="tasks")
public class Task {

    /*
    TODO
    - рекомендованный план выполнения
    - время выполнения
     */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_task_id")
    private Task parentTask;

    @Enumerated(value = EnumType.STRING)
    private TaskLevel level;

    private String skills;

    private String description;

    @OneToMany
    private List<Task> subtasks;

    @ManyToMany
    private List<Literature> recommended_literature;

    @ManyToMany
    private List<Student> active_students;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Enumerated(value = EnumType.STRING)
    private TaskStatus status;

}
