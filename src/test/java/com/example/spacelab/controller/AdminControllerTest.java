package com.example.spacelab.controller;

import com.example.spacelab.config.SecurityConfig;
import com.example.spacelab.config.TestSecurityConfig;
import com.example.spacelab.config.WithMockCustomUser;
import com.example.spacelab.dto.admin.AdminDTO;
import com.example.spacelab.dto.admin.AdminEditDTO;
import com.example.spacelab.mapper.AdminMapper;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.service.AdminService;
import com.example.spacelab.validator.AdminValidator;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@WithMockCustomUser
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService service;

    @MockBean
    private AdminMapper mapper;

    @Autowired
    private AdminValidator validator;

    static Admin admin;
    static List<Admin> adminList;
    static Page<Admin> adminPage;
    static AdminDTO dto;
    static AdminEditDTO editDTO;

    static ObjectMapper objectMapper = new JsonMapper();

    @BeforeAll
    public static void setupObjects() {
        admin = new Admin();
        admin.setId(1L);
        admin.setEmail("admin@gmail.com");
        admin.setPassword("admin");

        adminList = List.of(admin, admin);
        adminPage = new PageImpl<>(adminList);

        dto = new AdminDTO();

        editDTO = new AdminEditDTO();
        editDTO.setFirstName("test");
        editDTO.setLastName("test");
        editDTO.setPhone("+3908852721");
        editDTO.setRoleID(1L);
        editDTO.setEmail("test@gmail.com");
        editDTO.setPassword("teststst");
        editDTO.setConfirmPassword("teststst");
    }

    @BeforeEach
    public void setupMocks() {
        when(service.loadUserByUsername(any())).thenReturn(admin);

        when(service.getAdmins()).thenReturn(adminList);
        when(service.getAdmins(any())).thenReturn(adminPage);
        when(service.getAdmins(any(), any())).thenReturn(adminPage);
        when(service.getAdminById(anyLong())).thenReturn(admin);
        when(service.createAdmin(any())).thenReturn(admin);
        when(service.updateAdmin(any())).thenReturn(admin);

        when(mapper.fromAdminToDTO(any())).thenReturn(dto);
        when(mapper.fromEditDTOToAdmin(any())).thenReturn(admin);
    }

    @Test
    void getAdmins() throws Exception {

        MvcResult result = mockMvc.perform(get("/api/admins")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();


    }

    @Test
    void getAdmins_2() throws Exception {

        MvcResult result = mockMvc.perform(get("/api/admins?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();


    }

    @Test
    void getAdmin() throws Exception {

        Long id = 1L;

        MvcResult result = mockMvc.perform(get("/api/admins/{id}", id)
                                    .contentType(MediaType.APPLICATION_JSON))
                                    .andExpect(status().isOk())
                                    .andReturn();
    }

    @Test
    void createNewAdmin() throws Exception {


        MvcResult result = mockMvc.perform(post("/api/admins")
                        .content(objectMapper.writeValueAsString(editDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andReturn();

    }

    @Test
    void createNewAdmin_FailedValidation() throws Exception {

        AdminEditDTO dto1 = new AdminEditDTO();

        MvcResult result = mockMvc.perform(post("/api/admins")
                        .content(objectMapper.writeValueAsString(dto1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    void updateAdmin() throws Exception {

        Long id = 1L;

        MvcResult result = mockMvc.perform(put("/api/admins/{id}", id)
                        .content(objectMapper.writeValueAsString(editDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    void updateAdmin_FailedValidation() throws Exception {

        Long id = 1L;
        AdminEditDTO dto1 = new AdminEditDTO();

        MvcResult result = mockMvc.perform(put("/api/admins/{id}", id)
                        .content(objectMapper.writeValueAsString(dto1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    void deleteAdmin() throws Exception {

        Long id = 1L;

        MvcResult result = mockMvc.perform(delete("/api/admins/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

    }

    @Test
    void getAdminsByRole() throws Exception {

        Long id = 1L;

        MvcResult result = mockMvc.perform(get("/api/admins/get-admins-by-role?roleID=1&page=0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    void getAdminsWithoutCourses() throws Exception {

        MvcResult result = mockMvc.perform(get("/api/admins/get-available-admins")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();

    }
}
