package com.example.spacelab.integration.data;

public record UserAddResponse(
        Integer[] usersAdded,
        Integer[] usersAlreadyInProject,
        Integer[] usersNotAdded
) {
}
