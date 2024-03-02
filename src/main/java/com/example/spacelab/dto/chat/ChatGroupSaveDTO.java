package com.example.spacelab.dto.chat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record ChatGroupSaveDTO(
        @Min(value = -1)
        Long id,
        @NotBlank(message = "{validation.field.empty}")
        @Max(value = 100, message = "{validation.field.length.max}")
        String name,
        MultipartFile icon,
        Long[] admins,
        Long[] students
) {
}
