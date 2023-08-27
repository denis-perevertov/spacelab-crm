package com.example.spacelab.dto;

import com.example.spacelab.dto.course.CourseListDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AdminDTO {

    private Long id;

    private String full_name;

    @NotBlank
    private String firstName, lastName;

    @Pattern(regexp = "\\d{10,}")
    @NotBlank
    private String phone;

    @Email
    @NotBlank
    private String email;

    private String password, confirmPassword;

    @NotBlank
    private String role;

    @NotBlank
    private List<CourseListDTO> courses = new ArrayList<>();

}
