package com.example.spacelab.dto.literature;

import com.example.spacelab.model.literature.LiteratureType;
import lombok.Data;

@Data
public class LiteratureInfoDTO {
    private Long id;

    private String name;

    private Long courseId;
    private String courseName;

    private LiteratureType type;

    private String author_name;

    private String keywords;

    private String description;

    private String resource_link;

    private String img;
}
