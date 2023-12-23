package com.example.spacelab.dto.course;

import org.springframework.web.multipart.MultipartFile;

public record CourseIconDTO(
        MultipartFile icon
) {
}
