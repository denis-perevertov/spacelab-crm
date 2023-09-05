package com.example.spacelab.dto.literature;

import com.example.spacelab.model.literature.LiteratureType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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

    public LiteratureInfoDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
