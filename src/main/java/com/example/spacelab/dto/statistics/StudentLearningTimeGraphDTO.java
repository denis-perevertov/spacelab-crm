package com.example.spacelab.dto.statistics;

import java.util.List;

public record StudentLearningTimeGraphDTO(
        List<String> labels,
        List<Double> data
) {
}
