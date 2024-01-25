package com.example.spacelab.integration.teamwork.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TeamworkUserCreateRequest(
        Person person
) {
    public record Person(
            @JsonProperty("email-address")
            String email,
            @JsonProperty("first-name")
            String firstName,
            @JsonProperty("last-name")
            String lastName,
            @JsonProperty("send-invite")
            boolean sendInvite,
            String title,
            @JsonProperty("user-website")
            String github,
            @JsonProperty("user-linkedin")
            String linkedin,
            String language,
            boolean administrator,
            @JsonProperty("can-add-projects")
            boolean canAddProjects,
            @JsonProperty("can-access-all-projects")
            boolean canAccessAllProjects,
            @JsonProperty("set-project-admin")
            boolean setProjectAdmin,
            @JsonProperty("user-type")
            String userType
    ) {}
}
