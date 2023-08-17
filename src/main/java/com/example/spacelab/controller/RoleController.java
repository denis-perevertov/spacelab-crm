package com.example.spacelab.controller;

import com.example.spacelab.model.UserRole;
import com.example.spacelab.model.dto.UserRoleDTO;
import com.example.spacelab.service.UserRoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@Log
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController {

    private final UserRoleService userRoleService;

    @GetMapping
    public ResponseEntity<List<UserRoleDTO>> getRoles() {
        List<UserRoleDTO> list = userRoleService.getRoles();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRoleDTO> getRoleById(@PathVariable Long id) {
        UserRoleDTO role = userRoleService.getRoleById(id);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserRoleDTO> createNewRole(@Valid @RequestBody UserRoleDTO role) {
        role = userRoleService.createNewRole(role);
        return new ResponseEntity<>(role, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserRoleDTO> updateRole(@PathVariable Long id,
                                                  @Valid @RequestBody UserRoleDTO role) {
        role = userRoleService.createNewRole(role);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable Long id ){
        userRoleService.deleteRoleById(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }


}
