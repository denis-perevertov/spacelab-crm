package com.example.spacelab.dto.task;

import com.example.spacelab.model.task.TaskLevel;
import lombok.Data;

import java.util.Map;

@Data
public class TaskInfoDTO {

    private Long id;

    private String name;

    private Long parentTaskId;
    private String parentTaskName;

    private TaskLevel level;

    private String completionTime;

    private String skillsDescription;

    private String taskDescription;

    private Map<Long, String> subtasks;

    private Map<Long, String> recommendedLiterature;


}
