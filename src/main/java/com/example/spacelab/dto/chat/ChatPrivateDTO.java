package com.example.spacelab.dto.chat;

import com.example.spacelab.model.chat.ChatType;

import java.util.List;

public record ChatPrivateDTO(
        Long id,
        User user1,
        User user2,
        ChatType type,
        List<ChatMessageDTO> messages
) {
    public record User (
            Long id,
            String name,
            String role,
            String avatar
    ) {}
}
