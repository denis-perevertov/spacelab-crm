package com.example.spacelab.dto.lesson;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
public class LessonListDTO {

    @Schema(example = "10")
    private Long id;

    private LocalDateTime datetime; // or string

    @Schema(example = "3")
    private Long courseId;
    @Schema(example = "CourseName")
    private String courseName;

    @Schema(example = "http://www.link.com")
    private String link; // ?

    @Schema(example = "ACTIVE")
    private String status;

    @Schema(example = "3")
    private Long mentorId;
    @Schema(example = "MentorName")
    private String mentorName;

    @Schema(example = "4")
    private Long managerId;
    @Schema(example = "ManagerName")
    private String managerName;

    @Schema(example = "11")
    private String presentStudentsQuantity;


    public LessonListDTO(Long id, LocalDateTime datetime) {
        this.id = id;
        this.datetime = datetime;
    }
}
