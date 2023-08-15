package com.example.spacelab.controller;

import com.example.spacelab.model.UserRole;
import com.example.spacelab.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
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
    public List<UserRole> getRoles() {
        return new ArrayList<>();
    }

    @GetMapping("/{id}")
    public UserRole getRoleById(@PathVariable Long id) {
        return new UserRole();
    }

    @PostMapping
    public ResponseEntity<String> createNewRole(@RequestBody UserRole role) {
        return new ResponseEntity<>("Created", HttpStatusCode.valueOf(201));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateRole(@RequestBody UserRole role) {
        return new ResponseEntity<>("Updated", HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable Long id ){
        return new ResponseEntity<>("Deleted", HttpStatusCode.valueOf(200));
    }


}
