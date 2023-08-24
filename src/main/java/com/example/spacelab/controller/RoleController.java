package com.example.spacelab.controller;

import com.example.spacelab.mapper.RoleMapper;
import com.example.spacelab.dto.role.UserRoleDTO;
import com.example.spacelab.dto.role.UserRoleEditDTO;
import com.example.spacelab.model.role.UserRole;
import com.example.spacelab.service.UserRoleService;
import com.example.spacelab.validator.RoleValidator;
import com.example.spacelab.validator.ValidationErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Log
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController {

    private final UserRoleService userRoleService;
    private final RoleMapper roleMapper;
    private final RoleValidator validator;

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
    public ResponseEntity<?> createNewRole(@RequestBody UserRoleEditDTO dto,
                                           BindingResult bindingResult) {

        validator.validate(dto, bindingResult);

        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return new ResponseEntity<>(new ValidationErrorMessage(HttpStatus.BAD_REQUEST.value(), errors), HttpStatus.BAD_REQUEST);
        }

        UserRole role = userRoleService.createNewRole(roleMapper.fromEditDTOToRole(dto));
        return new ResponseEntity<>(roleMapper.fromRoleToDTO(role), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(@PathVariable Long id,
                                        @RequestBody UserRoleEditDTO dto,
                                        BindingResult bindingResult) {
        dto.setId(id);

        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return new ResponseEntity<>(new ValidationErrorMessage(HttpStatus.BAD_REQUEST.value(), errors), HttpStatus.BAD_REQUEST);
        }

        UserRole role = userRoleService.updateRole(roleMapper.fromEditDTOToRole(dto));
        return new ResponseEntity<>(roleMapper.fromRoleToDTO(role), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable Long id ){
        userRoleService.deleteRoleById(id);
        return new ResponseEntity<>("Role with ID: " + id + " deleted", HttpStatus.OK);
    }


}
