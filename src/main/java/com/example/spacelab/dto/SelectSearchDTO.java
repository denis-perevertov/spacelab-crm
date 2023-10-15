package com.example.spacelab.dto;

public record SelectSearchDTO(Long id, String text, String email, String course) {
    public SelectSearchDTO(Long id, String text) {
        this(id, text, "", "");
    }
}
