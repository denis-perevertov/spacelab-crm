package com.example.spacelab.dto.lesson;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

@Data
public class LessonReportRowSaveDTO {

    @Schema(example = "3")
    private Long id;

/*
    private Long lessonId;
    private Long studentId;
*/

    @Schema(example = "true")
    private Boolean wasPresent;

    @Schema(example = "11")
    private Double hours;

    @Schema(example = "note")
    private String hoursNote;

    @Schema(example = "comment")
    private String comment;

    @Schema(example = "3")
    private Integer rating;

    private Map<Long, Boolean> completedTasks;
}
