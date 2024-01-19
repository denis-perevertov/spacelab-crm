package com.example.spacelab.integration.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TaskListRequest(
        Long id,
        Boolean applyDefaultsToExistingTasks,
        TaskListDescription taskList
) {
}
