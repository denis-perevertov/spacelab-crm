package com.example.spacelab.integration.data;

public record UserRemoveRequest(
        String projectId,
        Remove remove
) {
    public record Add(String userIdList){}
    public record Remove(String userIdList){}
}
