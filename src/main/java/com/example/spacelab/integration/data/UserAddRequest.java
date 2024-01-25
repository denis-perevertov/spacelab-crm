package com.example.spacelab.integration.data;

public record UserAddRequest(
        String projectId,
        Integer[] userIds
) {
}
