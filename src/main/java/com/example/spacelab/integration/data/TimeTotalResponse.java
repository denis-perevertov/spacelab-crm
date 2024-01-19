package com.example.spacelab.integration.data;

public record TimeTotalResponse(
       TimeTotal taskTimeTotal,
       TimeTotal subtaskTimeTotal
) {
}
