package com.example.spacelab.dto.task;

import com.example.spacelab.dto.course.CourseListDTO;
import com.example.spacelab.model.task.TaskLevel;
import com.example.spacelab.model.task.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class TaskListDTO {

    @Schema(example = "3")
    private Long id;
    @NotBlank
    @Schema(example = "TaskName")
    private String name;
    @Schema(example = "ADVANCED")
    private TaskLevel level;
    @Schema(example = "ACTIVE")
    private TaskStatus status;

    @Schema(example = "3")
    private Long courseID;
    @Schema(example = "CourseName")
    private String courseName;

    public TaskListDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

//
//    private CourseListDTO course;
//    private String courseName;
}
