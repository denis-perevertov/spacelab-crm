package com.example.spacelab.dto.admin;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AdminLoginInfoDTO {
    private Long id;
    private String fullName;
    private String avatar;
    private String role;
    private List<Long> courses;
    private List<String> permissions;

    public AdminLoginInfoDTO(Long id, String fullName, String role, List<Long> courses, List<String> permissions) {
        this.id = id;
        this.fullName = fullName.trim();
        this.role = role.trim();
        this.courses = courses;
        this.permissions = permissions;
    }
}
