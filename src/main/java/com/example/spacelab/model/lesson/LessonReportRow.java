package com.example.spacelab.model.lesson;

import com.example.spacelab.model.student.Student;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.Length;

@Data
@Entity
@Table(name="lesson_report_rows")
@Accessors(chain = true)
public class LessonReportRow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Student student;

    private String currentTaskSnapshot;

    private Boolean wasPresent;

    private Integer rating;

    private Double hours;

    private String hoursNote;

    @Column(length = Length.LONG)
    private String comment;
}
