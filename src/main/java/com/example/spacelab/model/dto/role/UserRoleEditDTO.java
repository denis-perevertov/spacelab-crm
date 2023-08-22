package com.example.spacelab.model.dto.role;

import com.example.spacelab.model.role.PermissionSet;
import lombok.Data;

@Data
public class UserRoleEditDTO {

    private Long id;
    private String name;
    private PermissionSet permissions;

}
