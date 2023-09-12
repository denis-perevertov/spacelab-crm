package com.example.spacelab.dto.task;

import com.example.spacelab.dto.student.StudentAvatarDTO;
import com.example.spacelab.model.task.TaskLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class TaskInfoDTO {

    @Schema(example = "3")
    private Long id;

    @Schema(example = "TaskName")
    private String name;

    @Schema(example = "2")
    private Long parentTaskId;
    @Schema(example = "TaskName")
    private String parentTaskName;

    @Schema(example = "ADVANCED")
    private TaskLevel level;
    @Schema(example = "6-8 days")
    private String completionTime;
    @Schema(example = "Skills description")
    private String skillsDescription;
    @Schema(example = "Task Description")
    private String taskDescription;

    private Map<Long, String> subtasks = new HashMap<>();

    private Map<Long, String> recommendedLiterature = new HashMap<>();

    // для показа выполняющих задание студентов - ID, имя и аватарка
    private List<StudentAvatarDTO> students = new ArrayList<>();

    public TaskInfoDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }


}
