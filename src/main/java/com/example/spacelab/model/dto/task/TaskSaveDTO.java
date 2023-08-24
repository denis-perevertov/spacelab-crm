package com.example.spacelab.model.dto.task;

import com.example.spacelab.util.TaskLevel;
import com.example.spacelab.util.TaskStatus;
import lombok.Data;

import java.util.List;

@Data
public class TaskSaveDTO {

    private Long id;

    private String name;

    private Long parentTaskId;

    private Long courseId;

    private TaskLevel level;

    private String completionTime;

    private String skillsDescription;

    private String taskDescription;

    private List<Long> subtasksIds;

    private List<Long> recommendedLiteratureIds;

    private TaskStatus status;


}
