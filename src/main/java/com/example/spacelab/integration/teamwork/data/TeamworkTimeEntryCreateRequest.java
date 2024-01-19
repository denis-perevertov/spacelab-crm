package com.example.spacelab.integration.teamwork.data;

import com.example.spacelab.integration.data.TimeEntry;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public record TeamworkTimeEntryCreateRequest(
        TimeEntry timelog,
        Object[] tags,
        Object timelogOptions
) {

}
