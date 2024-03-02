package com.example.spacelab.api.example;

import com.example.spacelab.dto.statistics.StudentLearningTimeGraphDTO;
import com.example.spacelab.dto.statistics.StudentRatingGraphDTO;

public record StudentTimeRatingDistributionExample(
        StudentLearningTimeGraphDTO learningTime,
        StudentRatingGraphDTO rating
) {
}
