package com.example.spacelab.integration.data;

public record TaskListRequest(
        String projectId,
        Long id,
        Boolean applyDefaultsToExistingTasks,
        TaskListDescription taskList
) {
}
