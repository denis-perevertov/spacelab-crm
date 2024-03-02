package com.example.spacelab.dto.chat;

import com.example.spacelab.model.chat.ChatMessageStatus;
import com.example.spacelab.model.role.UserRole;

import java.time.ZonedDateTime;

public record ClientChatMessage(
        Long msgId,
        Long chatId,
        Sender sender,
        Recipient recipient,
        String content,
        ZonedDateTime datetime,
        ChatMessageStatus status
) {
    public record Sender(
            Long id,
            String name,
            String avatar,
            String role
    ) {}

    public record Recipient (
            Long id,
            String name,
            String avatar,
            String role
    ) {}
}
