package com.example.spacelab.dto.lesson;

import com.example.spacelab.model.lesson.LessonStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class LessonSaveBeforeStartDTO {

    @Schema(example = "10")
    private Long id;

    private LocalDateTime lessonStartTime;

    @Schema(example = "3")
    private Long courseID;

    @Schema(example = "ACTIVE")
    private LessonStatus status;

    @Schema(example = "http://www.link.com")
    private String link;

    @Schema(example = "true")
    private Boolean startsAutomatically;
}
