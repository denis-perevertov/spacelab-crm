package com.example.spacelab.integration.teamwork.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TeamworkUserResponse(
        @JsonProperty("STATUS")
        String status,
        String id
) {
}
