package com.example.spacelab.service.impl;

import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.model.role.UserRole;
import com.example.spacelab.repository.UserRoleRepository;
import com.example.spacelab.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    @Override
    public List<UserRole> getRoles() {
        log.info("Getting all user roles");
        return userRoleRepository.findAll();
    }

    @Override
    public UserRole getRoleById(Long id) {
        log.info("Getting role with ID: " + id);
        UserRole role = userRoleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        return role;
    }

    @Override
    public UserRole createNewRole(UserRole role) {
        role = userRoleRepository.save(role);
        log.info("Created role: " + role);
        return role;
    }

    @Override
    public UserRole updateRole(UserRole role) {
        role = userRoleRepository.save(role);
        log.info("Updated role: " + role);
        return role;
    }

    @Override
    public void deleteRoleById(Long id) {
        UserRole role = userRoleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        log.info("Deleting user role with ID: " + id);
        userRoleRepository.delete(role);
    }
}
