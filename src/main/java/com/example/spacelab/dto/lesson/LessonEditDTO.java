package com.example.spacelab.dto.lesson;

import com.example.spacelab.model.lesson.LessonStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class LessonEditDTO {

    @Schema(example = "10")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime lessonStartTime;

    @Schema(example = "3")
    private Long courseID;

    @Schema(example = "ACTIVE")
    private LessonStatus status;

    @Schema(example = "http://www.link.com")
    private String link;

    @Schema(example = "true")
    private Boolean startsAutomatically;

}
