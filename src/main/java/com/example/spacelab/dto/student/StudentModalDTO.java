package com.example.spacelab.dto.student;

public record StudentModalDTO(
        Long id,
        String name,
        String status,
        String email,
        String avatar,
        String courseName
) {
}
