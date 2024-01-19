package com.example.spacelab.integration.data;

public record TagResponse(
        Long id,
        Long projectId,
        String name,
        String color
) {
}
