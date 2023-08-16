package com.example.spacelab.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentTaskDTO {

    private Long id;
    private TaskDTO task;
    private LocalDate beginDate;
    private LocalDate endDate;
    private String status;
}
