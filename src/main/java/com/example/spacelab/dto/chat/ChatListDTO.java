package com.example.spacelab.dto.chat;

import java.util.List;

public record ChatListDTO(
        Long id,
        String name,
        String icon,
        List<ChatMessageDTO> messages
) {
}
