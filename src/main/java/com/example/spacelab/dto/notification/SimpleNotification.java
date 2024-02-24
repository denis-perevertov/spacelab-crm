package com.example.spacelab.dto.notification;

public record SimpleNotification(
        Long entityId,
        String entityName,
        String type,
        String avatar
) {
}
