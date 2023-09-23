package com.example.spacelab.dto.admin;

import lombok.Data;

import java.util.List;

@Data
public class AdminLoginInfoDTO {
    private Long id;
    private String fullName;
    private String role;
    private List<Long> courses;
    private List<String> permissions;
}
