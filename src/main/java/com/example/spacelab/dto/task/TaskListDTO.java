package com.example.spacelab.dto.task;

import com.example.spacelab.dto.course.CourseListDTO;
import com.example.spacelab.model.task.TaskLevel;
import com.example.spacelab.model.task.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
public class TaskListDTO {

    @Schema(example = "3")
    private Long id;
    @Schema(example = "TaskName")
    private String name;
    private TaskLinkDTO parentTask;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime createdAt;
    @Schema(example = "ADVANCED")
    private TaskLevel level;
    @Schema(example = "ACTIVE")
    private TaskStatus status;

    @Schema(example = "3")
    private Long courseID;
    @Schema(example = "CourseName")
    private String courseName;

    private String courseIcon;

}
