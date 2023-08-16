package com.example.spacelab.model.dto;

import com.example.spacelab.util.TaskLevel;
import com.example.spacelab.util.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class TaskDTO {

    private Long id;
    @NotBlank
    private String name;
    private TaskLevel level;
    private TaskStatus status;

    private CourseDTO course;
}
