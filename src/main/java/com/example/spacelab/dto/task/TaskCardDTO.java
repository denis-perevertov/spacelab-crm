package com.example.spacelab.dto.task;

import com.example.spacelab.model.task.TaskLevel;
import com.example.spacelab.model.task.TaskStatus;
import com.example.spacelab.util.TimeUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class TaskCardDTO {

    @Schema(example = "3")
    private Long id;

    @Schema(example = "TaskName")
    private String name;

    @Schema(example = "2")
    private Long parentTaskId;
    @Schema(example = "TaskName")
    private String parentTaskName;

    @Schema(example = "3")
    private Long courseId;
    @Schema(example = "CourseName")
    private String courseName;

    @Schema(example = "ADVANCED")
    private TaskLevel level;

    @Schema(example = "6-8 days")
    private String completionTime;

    private TimeUnit completionTimeUnit;

    @Schema(example = "Skills description")
    private String skillsDescription;

    @Schema(example = "Task Description")
    private String taskDescription;

    private Map<Long, String> subtasks;

    private Map<Long, String> recommendedLiterature;

    @Schema(example = "[1,2,3,4,5,6]")
    private List<Long> activeStudentsIds;

    @Schema(example = "ACTIVE")
    private TaskStatus status;

    public TaskCardDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
