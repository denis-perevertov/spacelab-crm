package com.example.spacelab.service;

import com.example.spacelab.model.role.UserRole;
import io.swagger.v3.oas.annotations.Hidden;

import java.util.List;
@Hidden
public interface UserRoleService {
    List<UserRole> getRoles();
    UserRole getRoleById(Long id);
    UserRole createNewRole(UserRole role);
    UserRole updateRole(UserRole role);
    void deleteRoleById(Long id);
}
