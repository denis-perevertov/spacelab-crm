package com.example.spacelab.model.student;

import com.example.spacelab.model.course.Course;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="invite_student_requests")
public class StudentInviteRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    private String firstName, lastName, fathersName, email, phone;

    @ManyToOne
    private Course course;
}
