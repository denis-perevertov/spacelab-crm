package com.example.spacelab.model.course;

import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.model.admin.Admin;
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
    private LocalDate beginningDate;
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate endDate;

    @OneToOne
    @JoinColumn(name = "mentor_id")
    private Admin mentor;

    @OneToOne
    @JoinColumn(name = "manager_id")
    private Admin manager;

    @OneToMany
    private List<Student> students;

    @OneToMany
    private List<Task> tasks;

    @Embedded
    private CourseInfo courseInfo;

    private Boolean isActive;

}
