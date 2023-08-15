package com.example.spacelab.controller;

import com.example.spacelab.model.Admin;
import com.example.spacelab.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@Log
@RequiredArgsConstructor
@RequestMapping("/admins")
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public List<Admin> getAdmins() {
        return new ArrayList<>();
    }

    @GetMapping("/{id}")
    public Admin getAdmin(@PathVariable Long id) {
        return new Admin();
    }

    @PostMapping
    public Admin createNewAdmin(@RequestBody Admin admin) {
        return new Admin();
    }

    @PutMapping("/{id}")
    public Admin updateAdmin(@RequestBody Admin admin) {
        return new Admin();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long id) {
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }
}
