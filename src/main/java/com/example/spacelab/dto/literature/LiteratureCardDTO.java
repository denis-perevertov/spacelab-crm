package com.example.spacelab.dto.literature;

import com.example.spacelab.model.literature.LiteratureType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class LiteratureCardDTO {
    private Long id;

    private String name;

    private Long courseId;
    private String courseName;

    private LiteratureType type;

    private String author_name;


    private String keywords;

    private String resource_link;

    private String description;

    private String img;

    public LiteratureCardDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
