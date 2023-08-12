package com.example.spacelab.service;

import com.example.spacelab.model.UserRole;

import java.util.List;

public interface UserRoleService {
    List<UserRole> getRoles();
    UserRole getRoleById(Long id);
    UserRole createNewRole(UserRole role);
    UserRole updateRole(UserRole role);
    void deleteRoleById(Long id);
}
