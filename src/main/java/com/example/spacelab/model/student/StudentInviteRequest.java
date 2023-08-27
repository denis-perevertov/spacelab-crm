package com.example.spacelab.model.student;

import com.example.spacelab.model.course.Course;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="invite_student_requests")
public class StudentInviteRequest {

    @Id
    private String id;

    private String firstName, lastName, fathersName, email, phone;

    @ManyToOne
    private Course course;
}
