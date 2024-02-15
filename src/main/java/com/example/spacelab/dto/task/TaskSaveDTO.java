package com.example.spacelab.dto.task;

import com.example.spacelab.model.task.TaskLevel;
import com.example.spacelab.model.task.TaskStatus;
import com.example.spacelab.util.TimeUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TaskSaveDTO {

    @Schema(example = "3")
    private Long id;

    @Schema(example = "TaskName")
    private String name;

    @Schema(example = "2")
    private Long parentTaskID; // ?

    @Schema(example = "3")
    private Long courseID;

    @Schema(example = "ADVANCED")
    private String level;

    @Schema(example = "ACTIVE")
    private String status;

    @Schema(example = "6-8 days")
    private String completionTime;

    private String completionTimeUnit;

    @Schema(example = "Skills description")
    private String skillsDescription;

    @Schema(example = "Task Description")
    private String taskDescription;

    @Schema(example = "[1,2,3,4,5]")
    private List<Long> subtasksIDs;

    private List<TaskLiteratureDTO> literatureList;

    private List<TaskProgressPointDTO> taskProgressPoints;

    public TaskSaveDTO(Long id, String name, Long parentTaskID, Long courseID, String level, String status, String completionTime, String completionTimeUnit, String skillsDescription, String taskDescription, List<Long> subtasksIDs, List<TaskLiteratureDTO> literatureList, List<TaskProgressPointDTO> taskProgressPoints) {
        this.id = id;
        this.name = name.trim();
        this.parentTaskID = parentTaskID;
        this.courseID = courseID;
        this.level = level.trim();
        this.status = status.trim();
        this.completionTime = completionTime.trim();
        this.completionTimeUnit = completionTimeUnit.trim();
        this.skillsDescription = skillsDescription.trim();
        this.taskDescription = taskDescription.trim();
        this.subtasksIDs = subtasksIDs;
        this.literatureList = literatureList;
        this.taskProgressPoints = taskProgressPoints;
    }
}
