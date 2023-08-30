package com.example.spacelab.dto.task;

import com.example.spacelab.model.task.TaskLevel;
import com.example.spacelab.model.task.TaskStatus;
import lombok.Data;

import java.util.List;

@Data
public class TaskSaveDTO {

    private Long id;

    private String name;

    private Long parentTaskId; // ?

    private Long courseId;

    private TaskLevel level;

    private TaskStatus status;

    private String completionTime;

    private String skillsDescription;

    private String taskDescription;

    private List<Long> subtasksIds;

    private List<Long> recommendedLiteratureIds;



}
