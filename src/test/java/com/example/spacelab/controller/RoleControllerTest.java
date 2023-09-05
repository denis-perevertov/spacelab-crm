package com.example.spacelab.controller;

import com.example.spacelab.config.SecurityConfig;
import com.example.spacelab.dto.role.UserRoleDTO;
import com.example.spacelab.dto.role.UserRoleEditDTO;
import com.example.spacelab.mapper.RoleMapper;
import com.example.spacelab.model.role.PermissionSet;
import com.example.spacelab.model.role.UserRole;
import com.example.spacelab.service.UserRoleService;
import com.example.spacelab.validator.RoleValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@WithUserDetails("admin@gmail.com")
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRoleService service;

    @MockBean
    private RoleMapper mapper;

    @Autowired
    private RoleValidator validator;

    static UserRole role;
    static List<UserRole> roleList;
    static UserRoleDTO dto;
    static List<UserRoleDTO> dtoList;
    static UserRoleEditDTO editDTO;

    static ObjectMapper objectMapper = new JsonMapper();

    @BeforeAll
    public static void setupObjects() {

        role = new UserRole();
        role.setName("Test");
        role.setId(1L);

        roleList = List.of(role, role);

        dto = new UserRoleDTO();
        dto.setId(1L);
        dto.setName("Test");
        dto.setPermissions(new PermissionSet());

        dtoList = List.of(dto, dto);

        editDTO = new UserRoleEditDTO();
        editDTO.setId(1L);
        editDTO.setName("Test");
        editDTO.setPermissions(new PermissionSet());

    }

    @BeforeEach
    public void setupMocks() {
        when(service.getRoles()).thenReturn(roleList);
        when(service.getRoleById(anyLong())).thenReturn(role);
        when(service.createNewRole(any())).thenReturn(role);
        when(service.updateRole(any())).thenReturn(role);

        when(mapper.fromRoleToDTO(any())).thenReturn(dto);
        when(mapper.fromDTOToRole(any())).thenReturn(role);
        when(mapper.fromEditDTOToRole(any())).thenReturn(role);

    }

    @Test
    void getRoles() throws Exception {

        mockMvc.perform(get("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

    }

    @Test
    void getRoleById() throws Exception {

        Long roleID = 1L;

        mockMvc.perform(get("/api/roles/{id}", roleID)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
    }

    @Test
    void createNewRole() throws Exception {

        mockMvc.perform(post("/api/roles")
                        .content(objectMapper.writeValueAsString(editDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andReturn();
    }

    @Test
    void createNewRole_FailedValidation() throws Exception {

        UserRoleEditDTO dto1 = new UserRoleEditDTO();

        mockMvc.perform(post("/api/roles")
                        .content(objectMapper.writeValueAsString(dto1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void updateRole() throws Exception {

        Long roleID = 1L;

        mockMvc.perform(put("/api/roles/{id}", roleID)
                        .content(objectMapper.writeValueAsString(editDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();
    }

    @Test
    void updateRole_FailedValidation() throws Exception {

        Long roleID = 1L;
        UserRoleEditDTO dto1 = new UserRoleEditDTO();

        mockMvc.perform(put("/api/roles/{id}", roleID)
                        .content(objectMapper.writeValueAsString(dto1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }
    @Test
    void deleteRole() throws Exception {

        Long roleID = 1L;

        mockMvc.perform(delete("/api/roles/{id}", roleID)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();
    }

    @Test
    void getAllRoles() throws Exception {

        mockMvc.perform(get("/api/roles/get-all-roles")
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk());


    }
}