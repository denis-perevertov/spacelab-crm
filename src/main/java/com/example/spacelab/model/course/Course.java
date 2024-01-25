package com.example.spacelab.model.course;

import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.literature.Literature;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.task.Task;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name="courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String trackingId;

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
    private Set<Student> students = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "course")
    private Set<Task> tasks = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "course")
    private Set<Literature> literature = new HashSet<>();

    @Embedded
    private CourseInfo courseInfo = new CourseInfo();

    @Enumerated(value = EnumType.STRING)
    private CourseStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id) && Objects.equals(trackingId, course.trackingId) && Objects.equals(name, course.name) && Objects.equals(icon, course.icon) && Objects.equals(beginningDate, course.beginningDate) && Objects.equals(endDate, course.endDate) && Objects.equals(courseInfo, course.courseInfo) && status == course.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trackingId, name, icon, beginningDate, endDate, courseInfo, status);
    }
}
