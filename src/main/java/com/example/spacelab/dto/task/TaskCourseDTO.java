package com.example.spacelab.dto.task;

import com.example.spacelab.model.task.TaskLevel;
import com.example.spacelab.model.task.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCourseDTO {

    private Long id;
    private int taskIndex;
    private String name;
    private TaskLevel level;
    private TaskStatus status;
    private List<TaskCourseDTO> subtasks;

}
