package com.example.spacelab.controller;

import com.example.spacelab.config.SecurityConfig;
import com.example.spacelab.dto.admin.AdminContactDTO;
import com.example.spacelab.dto.contact.ContactInfoDTO;
import com.example.spacelab.dto.contact.ContactInfoEditDTO;
import com.example.spacelab.mapper.ContactInfoMapper;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.contact.ContactInfo;
import com.example.spacelab.service.ContactInfoService;
import com.example.spacelab.validator.ContactValidator;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@WithUserDetails("admin@gmail.com")
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactInfoService service;

    @MockBean
    private ContactInfoMapper mapper;

    @Autowired
    private ContactValidator validator;

    static ContactInfo contact;
    static ContactInfoDTO contactDTO;
    static ContactInfoEditDTO contactEditDTO;
    static List<ContactInfo> contactList;
    static List<ContactInfoDTO> contactDTOList;
    static Page<ContactInfo> contactPage;

    static ObjectMapper objectMapper = new JsonMapper();

    @BeforeAll
    public static void setupObjects() {
        contact = new ContactInfo();
        contact.setEmail("test");
        contact.setPhone("test");
        contact.setTelegram("test");
        contact.setAdmin(new Admin());
        contact.setId(1L);
        contactList = List.of(contact, contact);
        contactPage = new PageImpl<>(contactList);

        contactDTO = new ContactInfoDTO();
        contactDTO.setId(1L);
        contactDTO.setEmail("test@gmail.com");
        contactDTO.setPhone("+380");
        contactDTO.setTelegram("@aabb");
        contactDTO.setAdmin(new AdminContactDTO());

        contactEditDTO = new ContactInfoEditDTO();
        contactEditDTO.setId(1L);
        contactEditDTO.setEmail("test@gmail.com");
        contactEditDTO.setPhone("+380");
        contactEditDTO.setTelegram("@aabb");
        contactEditDTO.setAdminID(1L);
    }

    @BeforeEach
    public void setupMocks() {
        when(service.getContacts()).thenReturn(contactList);
        when(service.getContacts(any())).thenReturn(contactPage);
        when(service.getContact(anyLong())).thenReturn(contact);
        when(service.saveContact(any(ContactInfo.class))).thenReturn(contact);
        when(service.editContact(any(ContactInfo.class))).thenReturn(contact);

        when(mapper.fromEditDTOToContact(any())).thenReturn(contact);
        when(mapper.fromContactToContactDTO(any())).thenReturn(contactDTO);
        when(mapper.fromContactDTOToContact(any())).thenReturn(contact);
    }

    @Test
    void getContacts() throws Exception {
        mockMvc.perform(get("/api/contacts?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
    }

    @Test
    void getContacts_2() throws Exception {
        mockMvc.perform(get("/api/contacts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getContact() throws Exception {

        Long contactID = 1L;

        mockMvc.perform(get("/api/contacts/{id}", contactID)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
    }

    @Test
    void createNewContact() throws Exception {


        MvcResult result = mockMvc.perform(post("/api/contacts")
                            .content(objectMapper.writeValueAsString(contactEditDTO))
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andReturn();

    }

    @Test
    void createNewContact_FailedValidation() throws Exception {

        ContactInfoEditDTO info = new ContactInfoEditDTO();

        MvcResult result = mockMvc.perform(post("/api/contacts")
                        .content(objectMapper.writeValueAsString(info))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        System.out.println(result.getResponse().getContentAsString());

    }

    @Test
    void editContact() throws Exception {

        Long contactID = 1L;

        MvcResult result = mockMvc.perform(put("/api/contacts/{id}", contactID)
                                    .content(objectMapper.writeValueAsString(contactEditDTO))
                                    .contentType(MediaType.APPLICATION_JSON))
                                    .andExpect(status().isOk())
                                    .andReturn();

    }

    @Test
    void editContact_FailedValidation() throws Exception {

        Long contactID = 1L;
        ContactInfoEditDTO info = new ContactInfoEditDTO();

        MvcResult result = mockMvc.perform(put("/api/contacts/{id}", contactID)
                        .content(objectMapper.writeValueAsString(info))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void deleteContact() throws Exception {

        Long contactID = 1L;

        MvcResult result = mockMvc.perform(delete("/api/contacts/{id}", contactID))
                                            .andExpect(status().isOk())
                                            .andReturn();

    }
}