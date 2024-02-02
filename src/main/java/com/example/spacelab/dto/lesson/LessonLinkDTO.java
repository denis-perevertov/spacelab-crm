package com.example.spacelab.dto.lesson;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.ZonedDateTime;

public record LessonLinkDTO(
        Long id,
        @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
        ZonedDateTime datetime
) {
}
