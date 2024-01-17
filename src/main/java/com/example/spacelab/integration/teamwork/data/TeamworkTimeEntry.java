package com.example.spacelab.integration.teamwork.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

public record TeamworkTimeEntry(
        Long id,
        Integer minutes,
        @JsonProperty("timeLogged")
        ZonedDateTime beginTime,
        Long userId,
        Long taskId,
        Long projectId
) {
}
