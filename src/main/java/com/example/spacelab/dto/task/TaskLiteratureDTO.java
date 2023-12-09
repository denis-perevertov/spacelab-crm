package com.example.spacelab.dto.task;

import com.example.spacelab.model.literature.LiteratureType;
import lombok.Data;

@Data
public class TaskLiteratureDTO {
    private Long id;
    private String name;
    private LiteratureType type;
}
