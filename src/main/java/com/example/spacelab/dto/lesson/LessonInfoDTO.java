package com.example.spacelab.dto.lesson;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class LessonInfoDTO {

    private Long id;

    private LocalDateTime datetime;

    private String status;

    private List<LessonReportRowDTO> lessonReportRowList;

    public LessonInfoDTO(Long id, LocalDateTime datetime, String status) {
        this.id = id;
        this.datetime = datetime;
        this.status = status;
    }
}
