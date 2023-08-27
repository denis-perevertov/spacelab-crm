package com.example.spacelab.dto.lesson;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class LessonListDTO {

    private Long id;

    private LocalDateTime datetime;

    private Long courseId;
    private String courseName;

    private String link;
    private String status;

    private Long mentorId;
    private String mentorName;

    private Long managerId;
    private String managerName;

    private String presentStudentsQuantity;




}
