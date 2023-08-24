package com.example.spacelab.model.student;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Embeddable
@Data
public class StudentDetails {

    private String firstName;
    private String lastName;
    private String fathersName;

    private String phone;
    private String email;
    private String telegram;
    private String address;

    private String githubLink;
    private String linkedinLink;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate birthdate;

    @Enumerated(value = EnumType.STRING)
    private StudentEducationLevel educationLevel;

    @Enumerated(value = EnumType.STRING)
    private StudentEnglishLevel englishLevel;

    @Enumerated(value = EnumType.STRING)
    private StudentWorkStatus workStatus;

    @Enumerated(value = EnumType.STRING)
    private StudentAccountStatus accountStatus;

}
