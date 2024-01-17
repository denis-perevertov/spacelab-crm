package com.example.spacelab.integration.teamwork.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TeamworkTimeTotalResponse(
        @JsonProperty("time-totals")
        TeamworkTimeTotal taskTimeTotal,
        @JsonProperty("subTasks")
        TeamworkTimeTotal subtaskTimeTotal
) {
}
