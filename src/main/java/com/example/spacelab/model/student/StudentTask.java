package com.example.spacelab.model.student;

import com.example.spacelab.model.task.Task;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
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
    @JoinColumn(nullable = false)
    private Task taskReference;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(nullable = false)
    private Student student;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_task_id")
    private StudentTask parentTask;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL)
    private List<StudentTask> subtasks;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate beginDate;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate endDate;

    @Enumerated(value = EnumType.STRING)
    private StudentTaskStatus status;

    private Integer percentOfCompletion;

}
