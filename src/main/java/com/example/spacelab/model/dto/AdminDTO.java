package com.example.spacelab.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AdminDTO {

    private Long id;

    @NotBlank
    private String full_name;
    private String firstName, lastName;

    @Pattern(regexp = "\\d{10,}")
    @NotBlank
    private String phone;

    @Email
    @NotBlank
    private String email;

    private String password;

    @NotBlank
    private String role;

    @NotBlank
    private String course;

}
