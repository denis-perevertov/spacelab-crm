package com.example.spacelab.dto.lesson;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class LessonListDTO {

    @Schema(example = "10")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime datetime; // or string

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


}
