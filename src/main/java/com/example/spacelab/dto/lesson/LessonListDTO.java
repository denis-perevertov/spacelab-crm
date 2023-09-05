package com.example.spacelab.dto.lesson;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
public class LessonListDTO {

    private Long id;

    private LocalDateTime datetime; // or string

    private Long courseId;
    private String courseName;

    private String link; // ?

    private String status;

    private Long mentorId;
    private String mentorName;

    private Long managerId;
    private String managerName;

    private String presentStudentsQuantity;


    public LessonListDTO(Long id, LocalDateTime datetime) {
        this.id = id;
        this.datetime = datetime;
    }
}
