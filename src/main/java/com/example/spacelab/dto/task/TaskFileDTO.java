package com.example.spacelab.dto.task;

import org.springframework.web.multipart.MultipartFile;

public record TaskFileDTO(
        Long id,
        String name,
        String link,
        MultipartFile file
) {
}
