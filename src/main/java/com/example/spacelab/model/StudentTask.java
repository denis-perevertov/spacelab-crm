package com.example.spacelab.model;

import com.example.spacelab.util.StudentTaskStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Entity
@Table(name="student_tasks")
public class StudentTask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Task task;

    @ManyToOne
    private Student student;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate beginDate;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate endDate;

    @Enumerated(value = EnumType.STRING)
    private StudentTaskStatus status;
}
