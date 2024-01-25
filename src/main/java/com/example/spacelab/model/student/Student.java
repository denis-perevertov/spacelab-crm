package com.example.spacelab.model.student;

import com.example.spacelab.model.UserEntity;
import com.example.spacelab.model.course.Course;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="students")
public class Student extends UserEntity {

    @Embedded
    private StudentDetails details = new StudentDetails();

    private String password;

    private Integer rating;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ToString.Exclude
    @JsonManagedReference
    @OneToMany(mappedBy = "student")
    private Set<StudentTask> tasks = new HashSet<>();

    private String taskTrackingProfileId;

    public String getFullName() {
        return String.join(" ",
                this.details.getFirstName(),
                this.details.getFathersName(),
                this.details.getLastName());
    }

    public String getInitials() {
        return this.details.getFirstName().charAt(0) + "." + this.details.getLastName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return Objects.equals(details, student.details) && Objects.equals(password, student.password) && Objects.equals(rating, student.rating) && Objects.equals(taskTrackingProfileId, student.taskTrackingProfileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), details, password, rating, taskTrackingProfileId);
    }
}
