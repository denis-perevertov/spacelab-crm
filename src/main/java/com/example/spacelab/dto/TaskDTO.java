package com.example.spacelab.dto;

import com.example.spacelab.model.task.TaskLevel;
import com.example.spacelab.model.task.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskDTO {

    private Long id;
    @NotBlank
    private String name;
    private TaskLevel level;
    private TaskStatus status;

    private CourseDTO course;
}
