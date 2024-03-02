package com.example.spacelab.dto.chat;

import java.util.List;

public record ChatsAndContactsDTO(
        List<Object> chats,
        List<Object> contacts
) {
}
