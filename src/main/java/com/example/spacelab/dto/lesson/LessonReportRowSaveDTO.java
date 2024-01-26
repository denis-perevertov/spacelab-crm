package com.example.spacelab.dto.lesson;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class LessonReportRowSaveDTO {

    @Schema(example = "3")
    private Long id;

    @Min(0)
    private Long lessonId;
    @Min(0)
    private Long studentId;

    @Schema(example = "true")
    private Boolean wasPresent;

    @Schema(example = "11")
    @Min(0)
    private Double hours;

    @Schema(example = "note")
    @Size(max = 255)
    private String note;

    @Schema(example = "comment")
    @Size(max = 1000)
    private String comment;

    @Schema(example = "3")
    @Min(0)
    private Integer rating;

    private List<Long> completedTasksIds;
    private List<Long> unlockedTasksIds;

    private boolean hoursNeedComment;
}
