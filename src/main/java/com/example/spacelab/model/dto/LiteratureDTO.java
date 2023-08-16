package com.example.spacelab.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LiteratureDTO {
    private Long id;
    @NotBlank(message = "Name must not be empty")
    private String name;
    private CourseDTO course;
    @NotBlank(message = "Type must not be empty")
    private String type;
    @NotBlank(message = "Author name must not be empty")
    private String author_name;
    private String keywords;
    @NotBlank(message = "Resource link must not be empty")
    private String resource_link;
}
