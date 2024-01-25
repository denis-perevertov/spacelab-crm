package com.example.spacelab.integration.teamwork.data;

public record TeamworkUserRemoveRequest(
        Remove remove
) {
    public record Add(String userIdList){}
    public record Remove(String userIdList){}
}
