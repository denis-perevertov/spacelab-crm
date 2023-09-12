package com.example.spacelab.dto.course;

import com.example.spacelab.model.course.CourseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class CourseSaveUpdatedDTO {

    @Schema(example = "10")
    private Long id;

    @Schema(example = "CourseName")
    private String name;

    private LocalDate beginDate;

    private LocalDate endDate;

    @Schema(example = "3")
    private Long mentorID;

    @Schema(example = "4")
    private Long managerID;

    @Schema(example = "[1,2,3,4,5]")
    private List<Long> students;

    @Schema(example = "[1,2,3,4,5]")
    private List<Long> tasks;

    @Schema(example = "[1,2,3,4,5]")
    private List<Long> literature;

    @Schema(example = "ACTIVE")
    private CourseStatus status;

    private CourseInformationDTO courseInfo;

    public CourseSaveUpdatedDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
