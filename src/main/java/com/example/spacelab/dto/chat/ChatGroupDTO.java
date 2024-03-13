package com.example.spacelab.dto.chat;

import com.example.spacelab.model.chat.ChatType;

import java.util.List;

public record ChatGroupDTO (
        Long id,
        String name,
        ChatType type,
        String icon,
        List<ChatMessageDTO> messages
) {
}
