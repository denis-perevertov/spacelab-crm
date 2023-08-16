package com.example.spacelab.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminDTO {

    private Long id;
    @NotBlank
    private String full_name;
    @NotBlank
    private String role;
    @NotBlank
    private String course;

}
