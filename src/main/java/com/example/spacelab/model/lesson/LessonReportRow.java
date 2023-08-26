package com.example.spacelab.model.lesson;

import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.student.StudentTask;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="lesson_report_rows")
public class LessonReportRow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Lesson lesson;

    private Boolean wasPresent;

    private Integer rating;
/*
    TODO исправить
    @ManyToMany
    private List<Task> currentTasks;
*/

    @ManyToMany
    private List<StudentTask> currentTasks;

    private Double hours;
    private String hoursNote;
    private String comment;
}
