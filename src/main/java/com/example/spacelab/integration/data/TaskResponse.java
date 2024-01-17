package com.example.spacelab.integration.data;

import java.time.ZonedDateTime;

public record TaskResponse(
        Long id,
        String name,
        String description,
        String priority,
        Integer progress,
        Long[] tagIds,
        String status,
        ZonedDateTime startDate,
        Long parentTaskId,
        Long tasklistId,
        Long[] assigneeIds
) {
}
