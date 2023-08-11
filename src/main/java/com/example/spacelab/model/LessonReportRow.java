package com.example.spacelab.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="lesson_reports_rows")
public class LessonReportRow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany
    private Student student;

    private Integer mark;

    @ManyToMany
    private List<Task> currentTasks;

    private Double hours;
    private String note;
    private String comment;
}
