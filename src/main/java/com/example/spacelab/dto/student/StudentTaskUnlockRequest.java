package com.example.spacelab.dto.student;

public record StudentTaskUnlockRequest(
        Long taskID,
        Long studentID
) {
}
