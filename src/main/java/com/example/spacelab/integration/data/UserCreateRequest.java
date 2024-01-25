package com.example.spacelab.integration.data;

public record UserCreateRequest(
        String email,
        String firstName,
        String lastName,
        boolean sendInvite,
        String title,
        String github,
        String linkedin,
        String language,
        boolean administrator,
        boolean canAddProjects,
        boolean canAccessAllProjects,
        boolean setProjectAdmin,
        String userType
) {
}
