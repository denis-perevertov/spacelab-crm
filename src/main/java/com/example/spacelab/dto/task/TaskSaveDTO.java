package com.example.spacelab.dto.task;

import com.example.spacelab.model.task.TaskLevel;
import com.example.spacelab.model.task.TaskStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TaskSaveDTO {

    private Long id;

    private String name;

    private Long parentTaskID; // ?

    private Long courseID;

    private TaskLevel level;

    private TaskStatus status;

    private String completionTime;

    private String skillsDescription;

    private String taskDescription;

    private List<Long> subtasksIDs;

    private List<Long> recommendedLiteratureIDs;


    public TaskSaveDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
