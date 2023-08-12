package com.example.spacelab.service.impl;

import com.example.spacelab.model.UserRole;
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
        return null;
    }

    @Override
    public UserRole getRoleById(Long id) {
        return null;
    }

    @Override
    public UserRole createNewRole(UserRole role) {
        return null;
    }

    @Override
    public UserRole updateRole(UserRole role) {
        return null;
    }

    @Override
    public void deleteRoleById(Long id) {

    }
}
