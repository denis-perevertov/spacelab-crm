package com.example.spacelab.integration.data;

import java.time.ZonedDateTime;

public record TimeEntryResponse(
        Long id,
        Integer minutes,
        ZonedDateTime beginTime,
        Long userId,
        Long taskId,
        Long projectId
) {
}
