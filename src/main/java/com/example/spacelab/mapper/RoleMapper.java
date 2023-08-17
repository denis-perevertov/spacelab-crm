package com.example.spacelab.mapper;

import com.example.spacelab.model.UserRole;
import com.example.spacelab.model.dto.UserRoleDTO;

public class RoleMapper {

    public UserRoleDTO fromRoleToDTO(UserRole role) {

        return new UserRoleDTO();
    }

    public UserRole fromDTOToRole(UserRoleDTO dto) {

        return new UserRole();
    }
}
