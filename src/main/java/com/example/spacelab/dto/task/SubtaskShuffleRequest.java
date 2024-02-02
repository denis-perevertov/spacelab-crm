package com.example.spacelab.dto.task;

public record SubtaskShuffleRequest(
        Long taskId,
        Long[] subtaskIds
) {
}
