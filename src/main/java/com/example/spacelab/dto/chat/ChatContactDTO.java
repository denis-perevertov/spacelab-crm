package com.example.spacelab.dto.chat;

public record ChatContactDTO(
        Long id,
        String name,
        String role,
        String avatar
) {
}
