package com.example.spacelab.dto.lesson;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class LessonReportRowDTO {

    private Long id;

    @Schema(example = "StudentFullName")
    private String student;

    @Schema(example = "true")
    private Boolean wasPresent;

    @Schema(example = "[\"task1\",\"task2\",\"task3\"]")
    private List<String> currentTasks;

    @Schema(example = "35.2")
    private Double hours;

    @Schema(example = "too many hours")
    private String hoursNote;

    @Schema(example = "comment")
    private String comment;

    @Schema(example = "7")
    private Integer rating;
}
