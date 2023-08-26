package com.example.spacelab.dto.lesson;

import lombok.Data;

import java.util.List;

@Data
public class LessonReportRowDTO {

    private Long id;

    private String student;

    private Boolean wasPresent;

    private List<String> currentTasks;

    private Double hours;

    private String hoursNote;

    private String comment;

    private Integer rating;
}