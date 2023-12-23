package com.example.spacelab.dto.task;

import com.example.spacelab.dto.course.CourseLinkDTO;
import com.example.spacelab.dto.student.StudentAvatarDTO;
import com.example.spacelab.model.task.TaskLevel;
import com.example.spacelab.util.TimeUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class TaskInfoDTO {

    @Schema(example = "3")
    private Long id;

    @Schema(example = "TaskName")
    private String name;

    private TaskLinkDTO parentTask;

    private CourseLinkDTO course;

    @Schema(example = "ADVANCED")
    private TaskLevel level;
    @Schema(example = "6-8 days")
    private String completionTime;
    private TimeUnit completionTimeUnit;
    @Schema(example = "Skills description")
    private String skillsDescription;
    @Schema(example = "Task Description")
    private String taskDescription;

    private List<TaskLinkDTO> subtasks = new ArrayList<>();

    private List<TaskLiteratureDTO> recommendedLiterature = new ArrayList<>();

    // для показа выполняющих задание студентов - ID, имя и аватарка
    private List<StudentAvatarDTO> students = new ArrayList<>();

    public TaskInfoDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }


}
