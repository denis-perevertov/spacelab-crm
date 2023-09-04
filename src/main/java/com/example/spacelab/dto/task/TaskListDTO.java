package com.example.spacelab.dto.task;

import com.example.spacelab.dto.course.CourseListDTO;
import com.example.spacelab.model.task.TaskLevel;
import com.example.spacelab.model.task.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskListDTO {

    private Long id;
    @NotBlank
    private String name;
    private TaskLevel level;
    private TaskStatus status;
    private CourseListDTO course;
    private String courseName;

    public TaskListDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
