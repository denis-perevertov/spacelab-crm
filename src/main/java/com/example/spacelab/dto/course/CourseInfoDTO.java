package com.example.spacelab.dto.course;

import com.example.spacelab.model.course.CourseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CourseInfoDTO {

    private Long id;

    @Schema(example = "CourseName")
    private String name;

    @Schema(example = "ACTIVE")
    private CourseStatus status;

    @Schema(example = "CourseDescription")
    private String main_description;

    @Schema(example = "[\"topic1\",\"topic2\",\"topic3\"]")
    private List<String> topics;

    @Schema(example = "6-8 months")
    private String completionTime;

    @Schema(example = "15")
    private Integer groupSize;

    public CourseInfoDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
