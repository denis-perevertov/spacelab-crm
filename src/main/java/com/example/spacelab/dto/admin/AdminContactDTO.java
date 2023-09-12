package com.example.spacelab.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AdminContactDTO {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "AdminFullName")
    private String fullName;
    @Schema(example = "ROLE")
    private String roleName;
    @Schema(example = "avatar.jpg")
    private String avatar;
}
