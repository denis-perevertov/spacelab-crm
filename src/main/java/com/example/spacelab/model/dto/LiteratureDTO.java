package com.example.spacelab.model.dto;

import lombok.Data;

@Data
public class LiteratureDTO {
    private Long id;
    private String name;
    private CourseDTO course;
    private String type;
    private String author_name;
    private String keywords;
    private String resource_link;
}
