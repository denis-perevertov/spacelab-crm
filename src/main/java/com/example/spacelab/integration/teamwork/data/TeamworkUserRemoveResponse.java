package com.example.spacelab.integration.teamwork.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TeamworkUserRemoveResponse(
        @JsonProperty("STATUS")
        String status,
        Details details
) {
    public record Details(
            String added,
            String removed,
            String failed
    ){}
}
