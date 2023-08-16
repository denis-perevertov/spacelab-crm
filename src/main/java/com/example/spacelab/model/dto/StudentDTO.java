package com.example.spacelab.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StudentDTO {

    private Long id;
    @NotBlank
    private String fullName;
    private CourseDTO courseDTO;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String phone;
    private String telegram;
    private Integer rating;
    private String status;

}
