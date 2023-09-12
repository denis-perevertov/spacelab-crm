package com.example.spacelab.dto.course;

import com.example.spacelab.model.course.CourseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseSelectDTO {

    @Schema(example = "10")
    private Long id;

    @Schema(example = "CourseName")
    private String name;

}
