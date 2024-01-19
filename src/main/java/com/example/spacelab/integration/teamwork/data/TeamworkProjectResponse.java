package com.example.spacelab.integration.teamwork.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TeamworkProjectResponse(
        @JsonProperty("STATUS")
        String status,
        String id
) {
}
