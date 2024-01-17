package com.example.spacelab.integration.teamwork.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

public record TeamworkTaskResponse (
        TeamworkTask task,
        Object meta,
        Object included
) {
}
