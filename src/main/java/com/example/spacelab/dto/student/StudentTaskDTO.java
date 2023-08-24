package com.example.spacelab.dto.student;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentTaskDTO {

    private Long id;
    private Long taskID;
    private LocalDate beginDate;
    private LocalDate endDate;
    private String status;
}
