package com.example.spacelab.model.dto.lesson;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class LessonSaveBeforeStartDTO {

    private Long id;

    private LocalDate date;
    private LocalTime time;

    private Long courseId;

    private Long mentorId;

    private Long managerId;

   private String link;

    private Boolean startsAutomatically;
}
