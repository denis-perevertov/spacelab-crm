package com.example.spacelab.dto.literature;

import lombok.Data;

@Data
public class LiteratureListDTO {
    private Long id;

    private String name;

    private Long courseId;
    private String courseName;

    private String type;

    private String author_name;

    private String keywords;

    private String resource_link;
}
