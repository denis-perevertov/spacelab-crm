package com.example.spacelab.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="lesson_reports")
public class LessonReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Lesson lesson;

    @OneToMany
    private List<LessonReportRow> rows;
}
