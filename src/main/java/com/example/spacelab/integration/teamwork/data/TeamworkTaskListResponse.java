package com.example.spacelab.integration.teamwork.data;

import java.util.List;

public record TeamworkTaskListResponse(
        List<TeamworkTask> tasks,
        Object meta,
        Object included
) {
}
