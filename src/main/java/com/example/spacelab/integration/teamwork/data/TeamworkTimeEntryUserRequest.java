package com.example.spacelab.integration.teamwork.data;

public record TeamworkTimeEntryUserRequest(
        String projectId,
        Integer userId,
        String fromdate,
        String fromtime,
        String todate,
        String totime,
        String sortby,
        Integer page,
        String pageSize
) {
}
