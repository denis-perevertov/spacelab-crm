package com.example.spacelab.dto.student;

public record StudentModalDTO(
        Long id,
        String name,
        String email,
        String image,
        String courseName
) {
}
