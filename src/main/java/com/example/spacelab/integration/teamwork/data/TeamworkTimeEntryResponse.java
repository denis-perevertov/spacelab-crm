package com.example.spacelab.integration.teamwork.data;

public record TeamworkTimeEntryResponse(
        TeamworkTimeEntry timelog,
        Object included
) {
}
