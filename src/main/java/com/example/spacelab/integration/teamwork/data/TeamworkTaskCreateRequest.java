package com.example.spacelab.integration.teamwork.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;

public record TeamworkTaskCreateRequest (
        Task task
) {
        public record Task(
                String name,
                String description,
                String priority,
                Integer progress,
                Long[] tagIds,
                String status,
                @JsonProperty("startAt")
                LocalDate startDate,
                Long parentTaskId,
                @JsonProperty("tasklistId")
                Long taskListId,
                TeamworkTaskAssignees assignees
        ) {

        }
}
