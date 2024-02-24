package com.example.spacelab.dto.chat;

public record ChatMemberDTO(
        Long id,
        String name,
        String avatar,
        String role
) {
}
