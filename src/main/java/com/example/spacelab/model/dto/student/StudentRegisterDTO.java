package com.example.spacelab.model.dto.student;

import com.example.spacelab.model.Student;
import com.example.spacelab.util.StudentEducationLevel;
import com.example.spacelab.util.StudentEnglishLevel;
import com.example.spacelab.util.StudentWorkStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
