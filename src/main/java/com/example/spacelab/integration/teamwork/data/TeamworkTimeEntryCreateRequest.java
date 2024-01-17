package com.example.spacelab.integration.teamwork.data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public record TeamworkTimeEntryCreateRequest(
        TimeEntry timelog,
        Object[] tags,
        Object timelogOptions
) {
    public record TimeEntry (
            LocalDate date,
            LocalTime time,
            String description,
            Integer hours,
            Integer minutes,
            Long taskId,
            Long userId
    ) {}
}
