package com.example.spacelab.integration.teamwork.data;

public record TeamworkUserAddResponse(
        Integer[] usersAdded,
        Integer[] usersAlreadyInProject,
        Integer[] usersNotAdded
) {
}
