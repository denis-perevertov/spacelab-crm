package com.example.spacelab.service.impl;

import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.mapper.RoleMapper;
import com.example.spacelab.model.UserRole;
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
        return userRoleRepository.findAll().stream()
                .map(roleMapper::fromRoleToDTO).toList();
    }

    @Override
    public UserRoleDTO getRoleById(Long id) {
        UserRole role = userRoleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        return roleMapper.fromRoleToDTO(role);
    }

    @Override
    public UserRoleDTO createNewRole(UserRoleDTO dto) {
        UserRole role = roleMapper.fromDTOToRole(dto);
        role = userRoleRepository.save(role);
        return roleMapper.fromRoleToDTO(role);
    }

    @Override
    public UserRoleDTO updateRole(UserRoleDTO dto) {
        UserRole role = roleMapper.fromDTOToRole(dto);
        role = userRoleRepository.save(role);
        return roleMapper.fromRoleToDTO(role);
    }

    @Override
    public void deleteRoleById(Long id) {
        userRoleRepository.deleteById(id);
    }
}
