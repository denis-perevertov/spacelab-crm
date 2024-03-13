package com.example.spacelab.controller;

import com.example.spacelab.api.AdminAPI;
import com.example.spacelab.dto.SelectDTO;
import com.example.spacelab.dto.admin.AdminAvatarDTO;
import com.example.spacelab.dto.admin.AdminDTO;
import com.example.spacelab.dto.admin.AdminEditDTO;
import com.example.spacelab.exception.ObjectValidationException;
import com.example.spacelab.mapper.AdminMapper;
import com.example.spacelab.mapper.CourseMapper;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.service.AdminService;
import com.example.spacelab.service.CourseService;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.validator.AdminValidator;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admins")
public class AdminController implements AdminAPI {

    private final AdminService adminService;
    private final AdminMapper adminMapper;
    private final AdminValidator adminValidator;

    private final CourseService courseService;
    private final CourseMapper courseMapper;

    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<?> getAdmins( FilterForm filters,
                                        @RequestParam(required = false, defaultValue = "0") Integer page,
                                        @RequestParam(required = false, defaultValue = "10") Integer size) {
        Page<AdminDTO> adminList;
        Page<Admin> adminPage;
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
        adminPage = adminService.getAdmins(filters.trim(), pageable);
        adminList = new PageImpl<>(adminPage.stream().map(adminMapper::fromAdminToDTO).toList(), pageable, adminPage.getTotalElements());

        return new ResponseEntity<>(adminList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAdmin(@PathVariable Long id) {
        Admin admin = adminService.getAdminById(id);
        return new ResponseEntity<>(adminMapper.fromAdminToDTO(admin), HttpStatus.OK);
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<?> getAdminCourses(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getAdminCourses(id).stream().map(courseMapper::fromCourseToCourseAdminDTO).toList());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createNewAdmin(@ModelAttribute AdminEditDTO admin,
                                            BindingResult bindingResult) throws IOException {

        admin.setId(null);
        adminValidator.validate(admin, bindingResult);

        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ObjectValidationException(errors);
        }

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminService.createAdmin(admin);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/edit")
    public ResponseEntity<?> getAdminForEdit(@PathVariable Long id) {
        return new ResponseEntity<>(adminMapper.fromAdminToEditDTO(adminService.getAdminById(id)), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateAdmin(@PathVariable Long id,
                                         @ModelAttribute AdminEditDTO admin,
                                         BindingResult bindingResult) throws IOException {

        admin.setId(id);

        adminValidator.validate(admin, bindingResult);

        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ObjectValidationException(errors);
        }

        if(admin.getPassword() != null && !admin.getPassword().isEmpty()) admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminService.updateAdmin(admin);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long id) {
        if(adminService.canDeleteAdmin(id)) {
            adminService.deleteAdminById(id);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    // ==================================

    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAvatar(@PathVariable Long id,
                                          @RequestPart MultipartFile avatar) throws IOException {
        adminService.uploadAvatarForAdmin(id, avatar);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/avatar")
    public ResponseEntity<?> deleteAvatar(@PathVariable Long id) {
        adminService.deleteAvatarForAdmin(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/select")
    public ResponseEntity<?> getAdminSelect() {
        return ResponseEntity.ok(adminService.getAdmins().stream().map(a -> new SelectDTO(a.getId().toString(), a.getFullName())).toList());
    }

    @GetMapping("/get-admins-list-by-role")
    public ResponseEntity<?> getAdminsListByRole(@RequestParam Long role) {
        FilterForm filters = FilterForm.with()
                .role(role)
                .build();
        return ResponseEntity.ok(
                adminService.getAdmins(filters).stream()
                        .map(a -> new AdminAvatarDTO(a.getId(), a.getFullName(), a.getAvatar()))
                        .toList()
        );
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAdminsWithoutCourses(FilterForm filters,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(adminService.getAdmins(filters.trim(), pageable).map(adminMapper::fromAdminToDTO));
    }
}
