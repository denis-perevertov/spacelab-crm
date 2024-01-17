package com.example.spacelab.integration.data;

public record UserResponse(
        String id,
        String status,
        User user
) {
    static class User {
        String id;
        String firstName;
        String lastName;
        String avatarUrl;
    }
}
