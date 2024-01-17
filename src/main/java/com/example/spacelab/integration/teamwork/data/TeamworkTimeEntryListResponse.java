package com.example.spacelab.integration.teamwork.data;

import java.util.List;

public record TeamworkTimeEntryListResponse(
        List<TeamworkTimeEntry> timelogs,
        Object meta,
        Object included
) {
}
