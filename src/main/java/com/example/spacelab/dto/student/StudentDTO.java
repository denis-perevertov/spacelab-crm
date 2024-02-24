package com.example.spacelab.dto.student;

import com.example.spacelab.dto.course.CourseLinkIconDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StudentDTO {

    @Schema(example = "3")
    private Long id;

    @Schema(example = "FullName")
    private String fullName;

    @NotBlank
    private String firstName, lastName, fathersName;

    @NotBlank
    @Email
    private CourseLinkIconDTO course;

    @Schema(example = "test@gmail.com")
    private String email;

    @NotBlank
    @Schema(example = "+380123456789")
    private String phone;

    @Schema(example = "@test")
    private String telegram;

    @Schema(example = "10")
    private Integer rating;

    @Schema(example = "ACTIVE")
    private String status;

    @Schema(example = "avatar.jpg")
    private String avatar;

}
