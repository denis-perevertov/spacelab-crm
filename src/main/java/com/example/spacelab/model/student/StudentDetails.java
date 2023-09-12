package com.example.spacelab.model.student;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Embeddable
@Data
public class StudentDetails {

    @Schema(example = "FirstName")
    private String firstName;
    @Schema(example = "LastName")
    private String lastName;
    @Schema(example = "FathersName")
    private String fathersName;

    @Schema(example = "+380123456789")
    private String phone;
    @Schema(example = "test@gmail.com")
    private String email;
    @Schema(example = "@test")
    private String telegram;
    @Schema(example = "Test address")
    private String address;

    @Schema(example = "http://www.github.com/link")
    private String githubLink;
    @Schema(example = "http://www.linkedin.com/link")
    private String linkedinLink;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate birthdate;

    @Enumerated(value = EnumType.STRING)
    @Schema(example = "BACHELOR")
    private StudentEducationLevel educationLevel;

    @Enumerated(value = EnumType.STRING)
    @Schema(example = "C1")
    private StudentEnglishLevel englishLevel;

    @Enumerated(value = EnumType.STRING)
    @Schema(example = "UNEMPLOYED")
    private StudentWorkStatus workStatus;

    @Enumerated(value = EnumType.STRING)
    @Schema(example = "ACTIVE")
    private StudentAccountStatus accountStatus;

}
