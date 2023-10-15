package com.example.spacelab.dto.course;

import com.example.spacelab.util.ProgramDuration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseSettingsDTO {

    private ProgramDuration programDuration;
    private Integer groupSize;
    private Integer hoursNorm;
}
