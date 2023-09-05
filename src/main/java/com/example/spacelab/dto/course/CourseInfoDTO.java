package com.example.spacelab.dto.course;

import com.example.spacelab.model.course.CourseStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CourseInfoDTO {

    private Long id;

    private String name;

    private CourseStatus status;

    private String main_description;

    private List<String> topics;

    private String completionTime;

    private Integer groupSize;

    public CourseInfoDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
