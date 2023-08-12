package com.example.spacelab.model.dto;

import com.example.spacelab.util.TaskLevel;
import com.example.spacelab.util.TaskStatus;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
//@Builder
@RequiredArgsConstructor
public class TaskDTO {

    private Long id;
    private String name;
    private TaskLevel level;
    private TaskStatus status;

    private CourseDTO course;
}
