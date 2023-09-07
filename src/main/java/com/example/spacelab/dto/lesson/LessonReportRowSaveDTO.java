package com.example.spacelab.dto.lesson;

import lombok.Data;

import java.util.Map;

@Data
public class LessonReportRowSaveDTO {

    private Long id;

/*
    private Long lessonId;
    private Long studentId;
*/

    private Boolean wasPresent;

    private Double hours;

    private String hoursNote;

    private String comment;

    private Integer rating;

    private Map<Long, Boolean> completedTasks;
}
