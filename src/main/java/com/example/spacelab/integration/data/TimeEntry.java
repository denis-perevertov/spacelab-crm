package com.example.spacelab.integration.data;

import java.time.LocalDate;
import java.time.LocalTime;

public record TimeEntry (
        String id,
        LocalDate date,
        LocalTime time,
        String description,
        Integer hours,
        Integer minutes,
        Long taskId,
        Long userId
) {

}
