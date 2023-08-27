package com.example.spacelab.model.student;

import com.example.spacelab.model.task.Task;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name="student_tasks")
public class StudentTask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_task_id")
    private StudentTask parentTask;

    @ManyToOne
    private Task task;

    @OneToMany
    private List<StudentTask> subtasks;

    @ManyToOne
    private Student student;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate beginDate;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate endDate;

    @Enumerated(value = EnumType.STRING)
    private StudentTaskStatus status;

    private Long percentOfCompletion;


}
