package com.example.spacelab.dto.task;

import java.util.List;

public record TaskProgressPointDTO(
        String name,
        List<TaskProgressPointDTO> subpoints
) {
}
