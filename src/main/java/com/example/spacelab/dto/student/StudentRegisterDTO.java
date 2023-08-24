package com.example.spacelab.dto.student;

import com.example.spacelab.model.student.StudentEducationLevel;
import com.example.spacelab.model.student.StudentEnglishLevel;
import com.example.spacelab.model.student.StudentWorkStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentRegisterDTO {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String fathersName;

    // todo address

    private LocalDate birthdate;

    @NotBlank
    private String phone;
    @NotBlank
    private String email;
    @NotBlank
    private String telegram;

    private String address;

    private String githubLink;
    private String linkedinLink;

    private StudentEducationLevel educationLevel;
    private StudentEnglishLevel englishLevel;
    private StudentWorkStatus workStatus;

    private String avatar;
}
