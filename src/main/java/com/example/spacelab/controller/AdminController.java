package com.example.spacelab.controller;

import com.example.spacelab.dto.SelectDTO;
import com.example.spacelab.dto.SelectSearchDTO;
import com.example.spacelab.dto.admin.*;
import com.example.spacelab.exception.ErrorMessage;
import com.example.spacelab.exception.ObjectValidationException;
import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.mapper.AdminMapper;
import com.example.spacelab.mapper.CourseMapper;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.dto.admin.AdminEditDTO;
import com.example.spacelab.service.AdminService;
import com.example.spacelab.service.CourseService;
import com.example.spacelab.util.AuthUtil;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.validator.AdminValidator;
import com.example.spacelab.exception.ValidationErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.java.Log;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name="Admin", description = "Admin controller")
@RestController
@Log
@ToString
@RequiredArgsConstructor
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;
    private final AdminMapper adminMapper;
    private final AdminValidator adminValidator;

    private final CourseService courseService;
    private final CourseMapper courseMapper;

    private final PasswordEncoder passwordEncoder;

    // Получение админов (с фильтрами и страницами)
    @Operation(description = "Get list of admins paginated by 'page/size' params (default values are 0/10)", summary = "Get Admins (page)", tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('settings.read.NO_ACCESS')")
    @GetMapping
    public ResponseEntity<?> getAdmins(@Parameter(name = "Filter object", description = "Collection of all filters for search results", required = false, example = "{}") FilterForm filters,
                                                    @RequestParam(required = false, defaultValue = "0") Integer page,
                                                    @RequestParam(required = false, defaultValue = "10") Integer size) {
        Page<AdminDTO> adminList;
        Page<Admin> adminPage;
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
        adminPage = adminService.getAdmins(filters.trim(), pageable);
        adminList = new PageImpl<>(adminPage.stream().map(adminMapper::fromAdminToDTO).toList(), pageable, adminPage.getTotalElements());

        return new ResponseEntity<>(adminList, HttpStatus.OK);
    }

    // Получение одного админа
    @Operation(description = "Get admin info by his ID: Name, Phone, Email, Role & Courses", summary = "Get Admin DTO By ID", tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('settings.read.NO_ACCESS')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getAdmin(@PathVariable @Parameter(example = "1") Long id) {
        Admin admin = adminService.getAdminById(id);
        return new ResponseEntity<>(adminMapper.fromAdminToDTO(admin), HttpStatus.OK);
    }

    @PreAuthorize("!hasAuthority('settings.read.NO_ACCESS')")
    @GetMapping("/{id}/courses")
    public ResponseEntity<?> getAdminCourses(@PathVariable @Parameter(example = "1") Long id) {
        return ResponseEntity.ok(courseService.getAdminCourses(id).stream().map(courseMapper::fromCourseToCourseAdminDTO).toList());
    }

    // Создание нового админа
    @Operation(description = "Create new admin in the application; ID field does not matter in write/edit operations", summary = "Create New Admin", tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful Creation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request / Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('settings.write.NO_ACCESS')")
    @PostMapping
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

    // Получение формы админа на редактирование
    @PreAuthorize("!hasAuthority('settings.read.NO_ACCESS')")
    @GetMapping("/{id}/edit")
    public ResponseEntity<?> getAdminForEdit(@PathVariable Long id) {
        return new ResponseEntity<>(adminMapper.fromAdminToEditDTO(adminService.getAdminById(id)), HttpStatus.OK);
    }

    // Редактирование админа
    @Operation(description = "Update existing admin in the application; ID field does not matter in write/edit operations", summary = "Update Admin", tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Update"),
            @ApiResponse(responseCode = "400", description = "Bad Request / Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('settings.edit.NO_ACCESS')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable @Parameter(example = "1") Long id,
                                                @ModelAttribute @io.swagger.v3.oas.annotations.parameters.RequestBody AdminEditDTO admin,
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

    // Удаление админа
    @Operation(description = "Delete admin by his ID", summary = "Delete Admin By ID", tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('settings.delete.NO_ACCESS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable @Parameter(example = "1") Long id) {
        if(adminService.canDeleteAdmin(id)) {
            adminService.deleteAdminById(id);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    // ==================================

    // Загрузка аватарки
    @PreAuthorize("!hasAuthority('settings.edit.NO_ACCESS')")
    @PostMapping("/{id}/avatar")
    public ResponseEntity<?> uploadAvatar(@PathVariable Long id,
                                          @RequestPart MultipartFile avatar) throws IOException {
        adminService.uploadAvatarForAdmin(id, avatar);
        return ResponseEntity.ok().build();
    }

    // Удаление аватарки
    @PreAuthorize("!hasAuthority('settings.edit.NO_ACCESS')")
    @DeleteMapping("/{id}/avatar")
    public ResponseEntity<?> deleteAvatar(@PathVariable Long id) {
        adminService.deleteAvatarForAdmin(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("!hasAuthority('settings.read.NO_ACCESS')")
    @GetMapping("/select")
    public ResponseEntity<?> getAdminSelect() {
        return ResponseEntity.ok(adminService.getAdmins().stream().map(a -> new SelectDTO(a.getId().toString(), a.getFullName())).toList());
    }

    @PreAuthorize("!hasAuthority('settings.read.NO_ACCESS')")
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

    // Получение списка незанятых админов (админов без назначенных курсов)
    @Operation(description = "Get list of admins not attached to any courses", summary = "Get Available Admins", tags = {"Admin"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('settings.read.NO_ACCESS')")
    @GetMapping("/available")
    public Page<AdminDTO> getAdminsWithoutCourses(FilterForm filters,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return adminService.getAdmins(filters.trim(), pageable).map(adminMapper::fromAdminToDTO);
    }
}
