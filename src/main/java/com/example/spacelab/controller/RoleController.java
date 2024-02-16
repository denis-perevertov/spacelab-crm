package com.example.spacelab.controller;

import com.example.spacelab.dto.SelectDTO;
import com.example.spacelab.dto.SelectSearchDTO;
import com.example.spacelab.dto.admin.AdminAvatarDTO;
import com.example.spacelab.dto.contact.ContactInfoDTO;
import com.example.spacelab.dto.role.UserRoleAdminListDTO;
import com.example.spacelab.dto.student.StudentAvatarDTO;
import com.example.spacelab.exception.ErrorMessage;
import com.example.spacelab.exception.ObjectValidationException;
import com.example.spacelab.mapper.RoleMapper;
import com.example.spacelab.dto.role.UserRoleDTO;
import com.example.spacelab.dto.role.UserRoleEditDTO;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.role.UserRole;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.repository.AdminRepository;
import com.example.spacelab.service.AdminService;
import com.example.spacelab.service.StudentService;
import com.example.spacelab.service.UserRoleService;
import com.example.spacelab.validator.RoleValidator;
import com.example.spacelab.exception.ValidationErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name="Role", description = "Role controller")
@RestController
@Log
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {

    private final UserRoleService userRoleService;
    private final StudentService studentService;
    private final AdminService adminService;
    private final AdminRepository adminRepository;
    private final RoleMapper roleMapper;
    private final RoleValidator validator;

    @Operation(description = "Get list of roles paginated by 'page/size' params (default values are 0/10)", summary = "Get Roles", tags = {"Role"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('roles.read.NO_ACCESS')")
    @GetMapping
    public ResponseEntity<List<UserRoleDTO>> getRoles() {
        List<UserRoleDTO> list = userRoleService.getRoles().stream().map(roleMapper::fromRoleToDTO).toList();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Operation(description = "Get role DTO by its ID", summary = "Get Role By ID", tags = {"Role"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Role not found in DB", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('roles.read.NO_ACCESS')")
    @GetMapping("/{id}")
    public ResponseEntity<UserRoleDTO> getRoleById(@PathVariable @Parameter(example = "1") Long id) {
        UserRoleDTO role = roleMapper.fromRoleToDTO(userRoleService.getRoleById(id));
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @Operation(description = "Create new role; ID field does not matter in write/edit operations", summary = "Create New Role", tags = {"Role"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created"),
            @ApiResponse(responseCode = "400", description = "Bad Request / Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('roles.write.NO_ACCESS')")
    @PostMapping
    public ResponseEntity<UserRoleDTO> createNewRole(@RequestBody UserRoleEditDTO dto,
                                           BindingResult bindingResult) {

        log.info(dto.toString());
        log.info(dto.getPermissions().toString());

        validator.validate(dto, bindingResult);

        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ObjectValidationException(errors);
        }
        UserRole role = userRoleService.createNewRole(roleMapper.fromEditDTOToRole(dto));
        return new ResponseEntity<>(roleMapper.fromRoleToDTO(role), HttpStatus.CREATED);
    }

    @Operation(description = "Update existing role in the application; ID field does not matter in write/edit operations", summary = "Update Role", tags = {"Role"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "400", description = "Bad Request / Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('roles.edit.NO_ACCESS')")
    @PutMapping("/{id}")
    public ResponseEntity<UserRoleDTO> updateRole(@PathVariable @Parameter(example = "1") Long id,
                                        @RequestBody UserRoleEditDTO dto,
                                        BindingResult bindingResult) {
        dto.setId(id);

        log.info(dto.toString());
        log.info(dto.getPermissions().toString());

        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ObjectValidationException(errors);
        }
        UserRole role = userRoleService.updateRole(roleMapper.fromEditDTOToRole(dto));
        return new ResponseEntity<>(roleMapper.fromRoleToDTO(role), HttpStatus.OK);
    }

    @Operation(description = "Delete role by his ID", summary = "Delete Role", tags = {"Role"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Role not found in DB", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('roles.delete.NO_ACCESS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable @Parameter(example = "1") Long id ){
        userRoleService.deleteRoleById(id);
        return new ResponseEntity<>("Role with ID: " + id + " deleted", HttpStatus.OK);
    }

    // ==============================

    // Получение списка ролей для Select2
    @Operation(description = "Endpoint for Select2, without endless pagination", summary = "Get Roles for Select2", tags = {"Lesson"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PreAuthorize("!hasAuthority('roles.read.NO_ACCESS')")
    @GetMapping("/members")
    public List<UserRoleAdminListDTO> getAllRolesMembers() {
        return userRoleService.getRoles()
                .stream()
                .map(role -> new UserRoleAdminListDTO(
                        role.getId(),
                        role.getName(),
                        role.getUserList().stream()
                                .map(user -> new AdminAvatarDTO(user.getId(), user.getUserEntityName(), user.getAvatar()))
                                .limit(50)
                                .toList()
                ))
                .toList();
    }

    @PreAuthorize("!hasAuthority('roles.read.NO_ACCESS')")
    @GetMapping("/get-roles-list")
    public List<SelectDTO> getRoleSelect() {
        return userRoleService.getRoles()
                .stream()
                .map(role -> new SelectDTO(role.getId().toString(), role.getName()))
                .toList();
    }


}
