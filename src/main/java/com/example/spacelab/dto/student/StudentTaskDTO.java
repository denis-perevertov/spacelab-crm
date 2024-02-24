package com.example.spacelab.dto.student;


import com.example.spacelab.dto.task.TaskListDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class StudentTaskDTO {

    @Schema(example = "3")
    private Long id;
    private TaskListDTO taskReference;
    @Schema(example = "3")
    private Long taskID;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private ZonedDateTime beginDate;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private ZonedDateTime endDate;
    @Schema(example = "ACTIVE")
    private String status;
}
