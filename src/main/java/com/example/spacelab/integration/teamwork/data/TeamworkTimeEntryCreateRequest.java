package com.example.spacelab.integration.teamwork.data;

import com.example.spacelab.integration.data.TimeEntry;

public record TeamworkTimeEntryCreateRequest(
        TimeEntry timelog,
        Object[] tags,
        Object timelogOptions
) {

}
