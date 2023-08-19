package com.example.spacelab.model.dto;

import com.example.spacelab.model.dto.TaskDTO.TaskListDTO;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentTaskDTO {

    private Long id;
    private TaskListDTO task;
    private LocalDate beginDate;
    private LocalDate endDate;
    private String status;
}
