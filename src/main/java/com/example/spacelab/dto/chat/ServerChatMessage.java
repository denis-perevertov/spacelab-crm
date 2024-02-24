package com.example.spacelab.dto.chat;

import com.example.spacelab.model.chat.ChatMessage;
import com.example.spacelab.model.chat.ChatMessageStatus;

import java.time.ZonedDateTime;

public record ServerChatMessage(
        Long msgId,
        Long chatId,
        Sender sender,
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

    public ServerChatMessage(ClientChatMessage clientMsg) {
        this(
                clientMsg.msgId(),
                clientMsg.chatId(),
                new Sender(
                        clientMsg.sender().id(),
                        clientMsg.sender().name(),
                        clientMsg.sender().avatar(),
                        clientMsg.sender().role()
                ),
                clientMsg.content(),
                clientMsg.datetime(),
                clientMsg.status()
        );
    }

    public ServerChatMessage(ChatMessage chatMsg) {
        this(
                chatMsg.getId(),
                chatMsg.getChat().getId(),
                new Sender(
                        chatMsg.getSender().getId(),
                        chatMsg.getSender().getUserEntityName(),
                        chatMsg.getSender().getAvatar(),
                        chatMsg.getSender().getRole().getName()
                ),
                chatMsg.getContent(),
                chatMsg.getDatetime(),
                chatMsg.getStatus()
        );
    }
}