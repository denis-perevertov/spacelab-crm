package com.example.spacelab.dto.chat;

import com.example.spacelab.model.chat.ChatMessageStatus;

import java.time.ZonedDateTime;

public record ChatMessageDTO(
        Long msgId,
        Long chatId,
        ChatMemberDTO sender,
        String content,
        ZonedDateTime datetime,
        ChatMessageStatus status
) {
}
