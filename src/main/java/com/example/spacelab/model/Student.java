package com.example.spacelab.model;

import com.example.spacelab.util.StudentAccountStatus;
import com.example.spacelab.util.StudentEducationLevel;
import com.example.spacelab.util.StudentEnglishLevel;
import com.example.spacelab.util.StudentWorkStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;
    private String fathersName;

    private String email;
    private String phone;
    private String telegram;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate birthdate;

    @Enumerated(value = EnumType.STRING)
    private StudentEducationLevel educationLevel;

    @Enumerated(value = EnumType.STRING)
    private StudentEnglishLevel englishLevel;

    @Enumerated(value = EnumType.STRING)
    private StudentWorkStatus workStatus;

    private Integer rating;

    @Enumerated(value = EnumType.STRING)
    private StudentAccountStatus accountStatus;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "student")
    private List<StudentTask> tasks = new ArrayList<>();

    private String password;
    @Transient private String confirmPassword;

    private String avatar;

    @ManyToOne
    private UserRole role;  // роль студента
}
