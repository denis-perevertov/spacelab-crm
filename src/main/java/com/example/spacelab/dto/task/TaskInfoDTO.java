package com.example.spacelab.dto.task;

import com.example.spacelab.dto.student.StudentAvatarDTO;
import com.example.spacelab.model.task.TaskLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class TaskInfoDTO {

    private Long id;

    private String name;

    private Long parentTaskId;
    private String parentTaskName;

    private TaskLevel level;
    private String completionTime;
    private String skillsDescription;
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
