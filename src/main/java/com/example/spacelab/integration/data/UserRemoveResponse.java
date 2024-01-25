package com.example.spacelab.integration.data;

public record UserRemoveResponse(
        String status,
        Details details
) {
    public record Details(
            String added,
            String removed,
            String failed
    ){}
}
