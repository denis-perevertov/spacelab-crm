package com.example.spacelab.dto.course;

import com.example.spacelab.util.ProgramDuration;
import lombok.Data;

public record CourseSettingsDTO(ProgramDuration programDuration, Integer groupSize, Integer hoursNorm) {

}
