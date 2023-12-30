package com.example.spacelab.model.course;

import com.example.spacelab.model.literature.Literature;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.model.admin.Admin;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="courses")
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    private String icon;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate beginningDate;
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Admin mentor;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Admin manager;

    @ToString.Exclude
    @OneToMany(mappedBy = "course")
    private List<Student> students = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "course")
    private List<Task> tasks = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "course")
    private List<Literature> literature = new ArrayList<>();

    @Embedded
    private CourseInfo courseInfo = new CourseInfo();

    @Enumerated(value = EnumType.STRING)
    private CourseStatus status;

    public Course(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
