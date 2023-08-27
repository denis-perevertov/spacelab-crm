package com.example.spacelab.dto;


import com.example.spacelab.dto.task.TaskListDTO;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentTaskDTO {

    private Long id;
    private TaskListDTO task;
    private Long taskID;
    private LocalDate beginDate;
    private LocalDate endDate;
    private String status;
}
