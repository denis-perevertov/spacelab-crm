package com.example.spacelab.integration.data;

import java.time.LocalDate;
import java.time.LocalTime;

public record TimeEntry (
        LocalDate date,
        LocalTime time,
        Integer minutes,
        Long projectId,
        Long taskId,
        Long userId
) {

}
