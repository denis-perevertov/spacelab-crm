package com.example.spacelab.service.impl;

import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.mapper.RoleMapper;
import com.example.spacelab.model.role.UserRole;
import com.example.spacelab.model.dto.UserRoleDTO;
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
    private final RoleMapper roleMapper;

    @Override
    public List<UserRoleDTO> getRoles() {
        log.info("Getting all user roles");
        return userRoleRepository.findAll().stream()
                .map(roleMapper::fromRoleToDTO).toList();
    }

    @Override
    public UserRoleDTO getRoleById(Long id) {
        log.info("Getting role with ID: " + id);
        UserRole role = userRoleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        return roleMapper.fromRoleToDTO(role);
    }

    @Override
    public UserRoleDTO createNewRole(UserRoleDTO dto) {
        log.info("Creating new user role from DTO: " + dto);
        UserRole role = roleMapper.fromDTOToRole(dto);
        log.info(role.toString());
        role = userRoleRepository.save(role);
        log.info("Created role: " + role);
        return roleMapper.fromRoleToDTO(role);
    }

    @Override
    public UserRoleDTO updateRole(UserRoleDTO dto) {
        log.info("Updating user role from DTO: " + dto);
        UserRole role = roleMapper.fromDTOToRole(dto);
        role = userRoleRepository.save(role);
        log.info("Updated role: " + role);
        return roleMapper.fromRoleToDTO(role);
    }

    @Override
    public void deleteRoleById(Long id) {
        log.info("Deleting user role with ID: " + id);
        userRoleRepository.deleteById(id);
    }
}
