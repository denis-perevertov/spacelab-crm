package com.example.spacelab.dto.student;

import com.example.spacelab.model.student.StudentEducationLevel;
import com.example.spacelab.model.student.StudentEnglishLevel;
import com.example.spacelab.model.student.StudentWorkStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentRegisterDTO {

    @NotBlank
    @Schema(example = "FirstName")
    private String firstName;
    @NotBlank
    @Schema(example = "LastName")
    private String lastName;
    @NotBlank
    @Schema(example = "FathersName")
    private String fathersName;

    // todo address

    private LocalDate birthdate;

    @NotBlank
    @Schema(example = "+380123456789")
    private String phone;
    @NotBlank
    @Schema(example = "test@gmail.com")
    private String email;
    @NotBlank
    @Schema(example = "@test")
    private String telegram;

    @Schema(example = "Test address")
    private String address;

    @Schema(example = "http://www.github.com/link")
    private String githubLink;
    @Schema(example = "http://www.linkedin.com/link")
    private String linkedinLink;

    @Schema(example = "BACHELOR")
    private StudentEducationLevel educationLevel;
    @Schema(example = "C1")
    private StudentEnglishLevel englishLevel;
    @Schema(example = "UNEMPLOYED")
    private StudentWorkStatus workStatus;

    @Schema(example = "avatar.jpg")
    private String avatar;
}
