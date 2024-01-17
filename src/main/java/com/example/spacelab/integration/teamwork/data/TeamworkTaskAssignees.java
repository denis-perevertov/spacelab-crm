package com.example.spacelab.integration.teamwork.data;

public record TeamworkTaskAssignees(
        Integer[] companyIds,
        Integer[] teamIds,
        Integer[] userIds
) {
}
