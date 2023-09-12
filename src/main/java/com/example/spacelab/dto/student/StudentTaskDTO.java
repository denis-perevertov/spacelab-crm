package com.example.spacelab.dto.student;


import com.example.spacelab.dto.task.TaskListDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentTaskDTO {

    @Schema(example = "3")
    private Long id;
    private TaskListDTO task;
    @Schema(example = "3")
    private Long taskID;
    private LocalDate beginDate;
    private LocalDate endDate;
    @Schema(example = "ACTIVE")
    private String status;
}
