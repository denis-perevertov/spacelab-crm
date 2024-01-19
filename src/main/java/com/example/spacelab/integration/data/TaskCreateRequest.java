package com.example.spacelab.integration.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record TaskCreateRequest(
        String name,
        String description,
        String priority,
        Integer progress,
        Long[] tagIds,
        String status,
        LocalDate startDate,
        Long parentTaskId,
        Long taskListId,
        Integer[] userIds
) {
}
