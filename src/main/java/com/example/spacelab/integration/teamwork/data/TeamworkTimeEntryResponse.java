package com.example.spacelab.integration.teamwork.data;

import java.util.List;

public record TeamworkTimeEntryResponse(
        TeamworkTimeEntry timelog,
        Object included
) {
}
