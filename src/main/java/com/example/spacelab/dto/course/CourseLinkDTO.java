package com.example.spacelab.dto.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CourseLinkDTO {
    private Long id;
    private String name;
    private String icon;
}
