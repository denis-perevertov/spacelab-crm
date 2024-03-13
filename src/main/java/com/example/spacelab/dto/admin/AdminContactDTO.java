package com.example.spacelab.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminContactDTO {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "AdminFullName")
    private String fullName;
    @Schema(example = "ROLE")
    private String roleName;
    @Schema(example = "avatar.jpg")
    private String avatar;

    public AdminContactDTO(Long id, String fullName, String roleName, String avatar) {
        this.id = id;
        this.fullName = fullName.trim();
        this.roleName = roleName.trim();
        this.avatar = avatar.trim();
    }
}
