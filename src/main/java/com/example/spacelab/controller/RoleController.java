package com.example.spacelab.controller;

import com.example.spacelab.mapper.RoleMapper;
import com.example.spacelab.dto.UserRoleDTO;
import com.example.spacelab.model.role.UserRole;
import com.example.spacelab.service.UserRoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Log
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController {

    private final UserRoleService userRoleService;
    private final RoleMapper roleMapper;

    @GetMapping
    public ResponseEntity<List<UserRoleDTO>> getRoles() {
        List<UserRoleDTO> list = userRoleService.getRoles().stream().map(roleMapper::fromRoleToDTO).toList();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRoleDTO> getRoleById(@PathVariable Long id) {
        UserRoleDTO role = roleMapper.fromRoleToDTO(userRoleService.getRoleById(id));
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserRoleDTO> createNewRole(@Valid @RequestBody UserRoleDTO dto) {
        UserRole role = userRoleService.createNewRole(roleMapper.fromDTOToRole(dto));
        return new ResponseEntity<>(roleMapper.fromRoleToDTO(role), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserRoleDTO> updateRole(@PathVariable Long id,
                                                  @Valid @RequestBody UserRoleDTO dto) {
        dto.setId(id);
        UserRole role = userRoleService.updateRole(roleMapper.fromDTOToRole(dto));
        return new ResponseEntity<>(roleMapper.fromRoleToDTO(role), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable Long id ){
        userRoleService.deleteRoleById(id);
        return new ResponseEntity<>("Role with ID: " + id + " deleted", HttpStatus.OK);
    }


}
