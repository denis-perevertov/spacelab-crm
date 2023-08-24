package com.example.spacelab.model.dto.lesson;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LessonInfoDTO {

    private Long id;

    private LocalDateTime datetime;

    private String status;

    private List<LessonReportRowDTO> lessonReportRowList;
}
