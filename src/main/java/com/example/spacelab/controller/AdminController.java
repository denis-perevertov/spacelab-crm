package com.example.spacelab.controller;

import com.example.spacelab.model.Admin;
import com.example.spacelab.model.dto.AdminDTO;
import com.example.spacelab.service.AdminService;
import com.example.spacelab.util.FilterForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.apache.coyote.Response;
import org.springframework.data.domain.PageRequest;
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
    public ResponseEntity<List<AdminDTO>> getAdmins(FilterForm filters,
                              @RequestParam(required = false) Integer page,
                              @RequestParam(required = false) Integer size) {
        List<AdminDTO> adminList = adminService.getAdmins(filters, PageRequest.of(page, size));
        return new ResponseEntity<>(adminList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminDTO> getAdmin(@PathVariable Long id) {
        return new ResponseEntity<>(adminService.getAdminById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AdminDTO> createNewAdmin(@Valid @RequestBody AdminDTO admin) {
        AdminDTO savedAdmin = adminService.createAdmin(admin);
        return new ResponseEntity<>(savedAdmin, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminDTO> updateAdmin(@PathVariable Long id,
                                                @Valid @RequestBody AdminDTO admin) {
        AdminDTO savedAdmin = adminService.updateAdmin(admin);
        return new ResponseEntity<>(savedAdmin, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdminById(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }
}
