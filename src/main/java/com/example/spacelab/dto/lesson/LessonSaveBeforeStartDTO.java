package com.example.spacelab.dto.lesson;

import com.example.spacelab.model.lesson.LessonStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;

@Data
public class LessonSaveBeforeStartDTO {

    @Schema(example = "10")
    private Long id;

    private ZonedDateTime lessonStartTime;

    @Schema(example = "3")
    private Long courseID;

    @Schema(example = "ACTIVE")
    private LessonStatus status = LessonStatus.PLANNED;

    @Schema(example = "http://www.link.com")
    private String link;

    @Schema(example = "true")
    private Boolean startsAutomatically;
}
