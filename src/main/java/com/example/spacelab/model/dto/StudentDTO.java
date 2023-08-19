package com.example.spacelab.model.dto;

import com.example.spacelab.model.dto.CourseDTO.CourseListDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StudentDTO {

    private Long id;

    private String fullName;

    @NotBlank
    private String firstName, lastName, fathersName;

    private CourseListDTO course;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phone;

    private String telegram;

    private Integer rating;

    private String status;

    private String avatar;

}
