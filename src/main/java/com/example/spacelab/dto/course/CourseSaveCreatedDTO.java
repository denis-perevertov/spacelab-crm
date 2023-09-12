package com.example.spacelab.dto.course;

import com.example.spacelab.model.course.CourseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class CourseSaveCreatedDTO {

    private Long id;

    @Schema(example = "NewCourse")
    private String name;

    @Schema(example = "2023-12-12")
    private LocalDate beginDate;

    @Schema(example = "3")
    private Long mentorID;

    @Schema(example = "4")
    private Long managerID;

    public CourseSaveCreatedDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
