package com.example.spacelab.model;

import com.example.spacelab.util.CourseStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name="courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate beginning_date;
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate end_date;

    @OneToOne
    @JoinColumn(name = "mentor_id")
    private Admin mentor;

    @OneToOne
    @JoinColumn(name = "manager_id")
    private Admin manager;

    @OneToMany
    private List<Student> students;

    @Embedded
    private CourseInfo courseInfo;

    private CourseStatus status;   // can replace with boolean active field

}
