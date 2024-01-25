package com.example.spacelab.integration.teamwork.data;

public record TeamworkTaskResponse (
        TeamworkTask task,
        Object meta,
        Object included
) {
}
