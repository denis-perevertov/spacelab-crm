package com.example.spacelab.model.dto.literature;

import lombok.Data;

import java.util.Map;

@Data
public class LiteratureCardDTO {
    private Long id;

    private String name;

    private Long courseId;
    private String courseName;

    private String type;

    private String author_name;

    private String keywords;

    private String resource_link;

    private String description;

    private Map<Long, String> courses;
}
