package com.example.spacelab.dto.course;

import com.example.spacelab.model.course.CourseStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CourseListDTO {

    @Schema(example = "10")
    private Long id;

    @Schema(example = "CourseName")
    private String name;

    private String icon;

    @Schema(example = "20")
    private Long studentsQuantity;

    private LocalDate beginDate;

    private String completionTime;
    private String completionTimeUnit;

    @Schema(example = "3")
    private Long mentorId;
    @Schema(example = "MentorName")
    private String mentorName;

    @Schema(example = "4")
    private Long managerId;
    @Schema(example = "ManagerName")
    private String managerName;

    @Schema(example = "ACTIVE")
    private CourseStatus status;

}
