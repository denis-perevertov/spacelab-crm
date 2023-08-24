package com.example.spacelab.model.dto.task;

import com.example.spacelab.util.TaskLevel;
import com.example.spacelab.util.TaskStatus;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TaskCardDTO {

    private Long id;

    private String name;

    private Long parentTaskId;
    private String parentTaskName;

    private Long courseId;
    private String courseName;

    private TaskLevel level;

    private String completionTime;

    private String skillsDescription;

    private String taskDescription;

    private Map<Long, String> subtasks;

    private Map<Long, String> recommendedLiterature;

    private List<Long> activeStudentsIds;

    private TaskStatus status;

    private Map<Long, String> courses;

}
