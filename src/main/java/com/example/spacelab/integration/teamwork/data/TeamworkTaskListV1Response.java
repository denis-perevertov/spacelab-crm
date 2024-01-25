package com.example.spacelab.integration.teamwork.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TeamworkTaskListV1Response(
        @JsonProperty("STATUS")
        String status,
        @JsonProperty("todo-items")
        List<TeamworkTaskV1> tasks
) {
    public record TeamworkTaskV1 (
            Long id,
            String parentTaskId,
            String status,
            String content,
            Boolean completed,
            Boolean canComplete,
            Tag[] tags
    ) {
       public record Tag (
               Long id,
               String name,
               String color,
               Long projectId
       ) {

       }
    }
}
