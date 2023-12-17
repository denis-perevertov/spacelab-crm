package com.example.spacelab.dto.task;

import com.example.spacelab.model.task.TaskLevel;
import com.example.spacelab.model.task.TaskStatus;
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
    private TaskLevel level;

    @Schema(example = "ACTIVE")
    private TaskStatus status;

    @Schema(example = "6-8 days")
    private String completionTime;

    @Schema(example = "Skills description")
    private String skillsDescription;

    @Schema(example = "Task Description")
    private String taskDescription;

    @Schema(example = "[1,2,3,4,5]")
    private List<Long> subtasksIDs;

    private List<TaskLiteratureDTO> literatureList;

    public TaskSaveDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
