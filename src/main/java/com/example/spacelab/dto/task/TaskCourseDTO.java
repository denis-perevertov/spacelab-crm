package com.example.spacelab.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCourseDTO {

    private Long id;
    private String name;
    private List<TaskCourseDTO> subtasks;

}
