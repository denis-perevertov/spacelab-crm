package com.example.spacelab.model.student;

import com.example.spacelab.model.UserEntity;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.lesson.LessonReportRow;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
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
    private List<StudentTask> tasks = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "student")
    private List<LessonReportRow> lessonData = new ArrayList<>();

    public String getFullName() {
        return String.join(" ",
                this.details.getFirstName(),
                this.details.getFathersName(),
                this.details.getLastName());
    }


}
