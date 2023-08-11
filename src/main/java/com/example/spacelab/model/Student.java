package com.example.spacelab.model;

import com.example.spacelab.util.StudentAccountStatus;
import com.example.spacelab.util.StudentEducationLevel;
import com.example.spacelab.util.StudentEnglishLevel;
import com.example.spacelab.util.StudentWorkStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Entity
@Table(name="students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String first_name;
    private String last_name;
    private String fathers_name;

    private String email;
    private String phone;
    private String telegram;
    private String viber;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate birthdate;

    @Enumerated(value = EnumType.STRING)
    private StudentEducationLevel education_level;

    @Enumerated(value = EnumType.STRING)
    private StudentEnglishLevel english_level;

    @Enumerated(value = EnumType.STRING)
    private StudentWorkStatus work_status;

    private Integer rating;

    @Enumerated(value = EnumType.STRING)
    private StudentAccountStatus account_status;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
