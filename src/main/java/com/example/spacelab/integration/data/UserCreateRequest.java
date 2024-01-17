package com.example.spacelab.integration.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserCreateRequest(
        @JsonProperty("emailAddress")
        String email,
        String firstName,
        String lastName,
        boolean sendInvite,
        String title,
        @JsonProperty("userWebsite")
        String github,
        @JsonProperty("userLinkedin")
        String linkedin,
        String language,
        boolean administrator,
        boolean canAddProjects,
        boolean canAccessAllProjects,
        boolean setProjectAdmin,
        String userType
) {
}
