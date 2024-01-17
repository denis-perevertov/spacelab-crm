package com.example.spacelab.integration.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TaskListCreateRequest(
        Boolean applyDefaultsToExistingTasks,
        @JsonProperty("todo-list")
        TaskListDescription taskList
) {
}
