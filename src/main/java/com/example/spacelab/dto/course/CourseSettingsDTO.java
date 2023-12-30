package com.example.spacelab.dto.course;

import com.example.spacelab.util.ProgramDuration;
import com.example.spacelab.util.TimeUnit;
import lombok.Data;

public record CourseSettingsDTO(
        String completionTime,
        TimeUnit completionTimeUnit,
        Integer groupSize,
        Integer hoursNorm
) {

}
