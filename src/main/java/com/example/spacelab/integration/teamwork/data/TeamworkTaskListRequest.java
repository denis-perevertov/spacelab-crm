package com.example.spacelab.integration.teamwork.data;

import com.example.spacelab.integration.data.TaskListDescription;
import com.fasterxml.jackson.annotation.JsonProperty;

public record TeamworkTaskListRequest(
        Long id,
        Boolean applyDefaultsToExistingTasks,
        @JsonProperty("todo-list")
        TaskListDescription taskList
) {
}
