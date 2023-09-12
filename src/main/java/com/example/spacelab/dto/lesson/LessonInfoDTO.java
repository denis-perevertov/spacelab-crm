package com.example.spacelab.dto.lesson;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class LessonInfoDTO {

    @Schema(example = "10")
    private Long id;

    private LocalDateTime datetime;

    @Schema(example = "ACTIVE")
    private String status;

    private List<LessonReportRowDTO> lessonReportRowList;

    public LessonInfoDTO(Long id, LocalDateTime datetime, String status) {
        this.id = id;
        this.datetime = datetime;
        this.status = status;
    }
}
