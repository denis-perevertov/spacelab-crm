package com.example.spacelab.dto.literature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiteratureSaveDTO {
    private Long id;

    private String name;

    private Long courseId;

    private String type;

    private String author_name;

    private String keywords;

    private String description;

    private String resource_link;

    public LiteratureSaveDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
