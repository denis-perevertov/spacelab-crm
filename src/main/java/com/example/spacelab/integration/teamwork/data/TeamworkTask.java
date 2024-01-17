package com.example.spacelab.integration.teamwork.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

public record TeamworkTask (
        Long id,
        String name,
        String description,
        String priority,
        Integer progress,
        Long[] tagIds,
        String status,
        ZonedDateTime startDate,
        Long parentTaskId,
        @JsonProperty("tasklistId")
        Long taskListId,
        @JsonProperty("assigneeUserIds")
        Long[] assigneeIds
) {
}
