package com.example.spacelab.model.dto.task;

import com.example.spacelab.model.dto.course.CourseListDTO;
import com.example.spacelab.util.TaskLevel;
import com.example.spacelab.util.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskListDTO {

    private Long id;
    @NotBlank
    private String name;
    private TaskLevel level;
    private TaskStatus status;
    private CourseListDTO course;
    private String courseName;
}
