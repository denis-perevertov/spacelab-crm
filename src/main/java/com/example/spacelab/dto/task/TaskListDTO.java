package com.example.spacelab.dto.task;

import com.example.spacelab.dto.course.CourseListDTO;
import com.example.spacelab.model.task.TaskLevel;
import com.example.spacelab.model.task.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Map;

@Data
public class TaskListDTO {

    private Long id;
    @NotBlank
    private String name;
    private TaskLevel level;
    private TaskStatus status;

    private Long courseID;
    private String courseName;
//
//    private CourseListDTO course;
//    private String courseName;
}
