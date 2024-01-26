package com.example.spacelab.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class StudentTaskPointDTO {
        Long id;
        Long parentTaskId;
        Long taskListId;
        String name;
        String description;
        String priority;
        Integer progress;
        @JsonFormat(pattern = "dd.MM.yyyy")
        ZonedDateTime startDate;
        String status;
        Integer loggedMinutes;
        Integer estimatedMinutes;
        List<StudentTaskTagDTO> tags;
        List<StudentTaskPointDTO> subpoints;
}
