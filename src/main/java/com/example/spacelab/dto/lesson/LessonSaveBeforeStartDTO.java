package com.example.spacelab.dto.lesson;

import com.example.spacelab.model.lesson.LessonStatus;
import com.example.spacelab.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
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

    public LessonSaveBeforeStartDTO(Long id, ZonedDateTime lessonStartTime, Long courseID, LessonStatus status, String link, Boolean startsAutomatically) {
        this.id = id;
        this.lessonStartTime = lessonStartTime;
        this.courseID = courseID;
        this.status = status;
        this.link = StringUtils.trimString(link);
        this.startsAutomatically = startsAutomatically;
    }
}
