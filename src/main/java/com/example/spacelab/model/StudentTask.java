package com.example.spacelab.model;

import com.example.spacelab.util.UserTaskStatus;
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
    private LocalDate begin_date;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate end_date;

    @Enumerated(value = EnumType.STRING)
    private UserTaskStatus status;
}
