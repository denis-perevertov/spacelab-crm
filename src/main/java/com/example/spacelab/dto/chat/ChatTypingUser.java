package com.example.spacelab.dto.chat;

public record ChatTypingUser(
        Long id,
        String name,
        TypingEvent event
) {
    public enum TypingEvent {
        START,
        STOP
    }
}
