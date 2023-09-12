package com.example.spacelab.dto.role;

import com.example.spacelab.model.role.PermissionSet;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserRoleEditDTO {

    @Schema(example = "3")
    private Long id;
    @Schema(example = "RoleName")
    private String name;
    private PermissionSet permissions;

}
