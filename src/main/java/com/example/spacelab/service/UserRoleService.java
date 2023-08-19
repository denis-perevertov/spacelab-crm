package com.example.spacelab.service;

import com.example.spacelab.model.dto.UserRoleDTO;

import java.util.List;

public interface UserRoleService {
    List<UserRoleDTO> getRoles();
    UserRoleDTO getRoleById(Long id);
    UserRoleDTO createNewRole(UserRoleDTO role);
    UserRoleDTO updateRole(UserRoleDTO role);
    void deleteRoleById(Long id);
}
