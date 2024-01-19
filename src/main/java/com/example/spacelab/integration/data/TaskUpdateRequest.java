package com.example.spacelab.integration.data;

import java.time.LocalDate;

public record TaskUpdateRequest(
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
