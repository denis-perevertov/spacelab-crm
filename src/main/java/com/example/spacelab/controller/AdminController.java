package com.example.spacelab.controller;

import com.example.spacelab.mapper.AdminMapper;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.dto.AdminDTO;
import com.example.spacelab.dto.admin.AdminEditDTO;
import com.example.spacelab.service.AdminService;
import com.example.spacelab.util.FilterForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Log
@RequiredArgsConstructor
@RequestMapping("/admins")
public class AdminController {

    private final AdminService adminService;
    private final AdminMapper adminMapper;

    // Получение админов (с фильтрами и страницами)
    @GetMapping
    public ResponseEntity<Page<AdminDTO>> getAdmins(FilterForm filters,
                                                    @RequestParam(required = false) Integer page,
                                                    @RequestParam(required = false) Integer size) {
        Page<AdminDTO> adminList;
        if(page == null || size == null) adminList = new PageImpl<>(adminService.getAdmins().stream().map(adminMapper::fromAdminToDTO).toList());
        else adminList = new PageImpl<>(adminService.getAdmins(filters, PageRequest.of(page, size)).stream().map(adminMapper::fromAdminToDTO).toList());
        return new ResponseEntity<>(adminList, HttpStatus.OK);
    }

    // Получение одного админа
    @GetMapping("/{id}")
    public ResponseEntity<AdminDTO> getAdmin(@PathVariable Long id) {
        Admin admin = adminService.getAdminById(id);
        return new ResponseEntity<>(adminMapper.fromAdminToDTO(admin), HttpStatus.OK);
    }

    // Создание нового админа
    @PostMapping
    public ResponseEntity<AdminDTO> createNewAdmin(@Valid @RequestBody AdminEditDTO admin) {
        Admin savedAdmin = adminService.createAdmin(adminMapper.fromEditDTOToAdmin(admin));
        return new ResponseEntity<>(adminMapper.fromAdminToDTO(savedAdmin), HttpStatus.CREATED);
    }

    // Редактирование админа
    @PutMapping("/{id}")
    public ResponseEntity<AdminDTO> updateAdmin(@PathVariable Long id,
                                                @Valid @RequestBody AdminEditDTO admin) {
        admin.setId(id);
        Admin savedAdmin = adminService.updateAdmin(adminMapper.fromEditDTOToAdmin(admin));
        return new ResponseEntity<>(adminMapper.fromAdminToDTO(savedAdmin), HttpStatus.OK);
    }

    // Удаление админа
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdminById(id);
        return new ResponseEntity<>("Admin with ID: " + id + " deleted", HttpStatus.OK);
    }
}
