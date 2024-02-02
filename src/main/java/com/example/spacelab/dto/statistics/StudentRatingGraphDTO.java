package com.example.spacelab.dto.statistics;

import java.util.List;

public record StudentRatingGraphDTO(
    List<String> labels,
    List<Integer> rating
) {
}
