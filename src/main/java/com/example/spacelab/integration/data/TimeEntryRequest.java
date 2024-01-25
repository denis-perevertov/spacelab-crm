package com.example.spacelab.integration.data;

public record TimeEntryRequest(
        String id,
        TimeEntry timelog,
        Object[] tags,
        Object timelogOptions
) {
}