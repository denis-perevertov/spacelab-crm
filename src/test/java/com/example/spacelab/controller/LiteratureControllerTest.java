package com.example.spacelab.controller;

import com.example.spacelab.config.SecurityConfig;
import com.example.spacelab.dto.literature.*;
import com.example.spacelab.mapper.LiteratureMapper;
import com.example.spacelab.model.literature.Literature;
import com.example.spacelab.service.LiteratureService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class LiteratureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LiteratureService literatureService;

    @MockBean
    private LiteratureMapper literatureMapper;


    @Test
    @WithMockUser(username = "user", authorities = {"literature.read.ACCESS"})
    public void testGetLiterature() throws Exception {
        Page<Literature> literaturePage = new PageImpl<>(Arrays.asList(new Literature(1L, "Sample Book")));
        when(literatureService.getLiterature(any(), any())).thenReturn(literaturePage);
        when(literatureMapper.fromPageLiteraturetoPageDTOList(any())).thenReturn(
                new PageImpl<>(Collections.singletonList(new LiteratureListDTO(1L, "Sample Book"))));

        mockMvc.perform(get("/api/literature").param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Sample Book"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"literature.read.ACCESS"})
    public void testGetLiteratureById() throws Exception {
        Literature literature = new Literature(1L, "Sample Book");
        when(literatureService.getLiteratureById(1L)).thenReturn(literature);
        when(literatureMapper.fromLiteraturetoInfoDTO(any())).thenReturn(
                new LiteratureInfoDTO(1L, "Sample Book"));

        mockMvc.perform(get("/api/literature/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Sample Book"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"literature.read.ACCESS"})
    public void testVerifyLiterature() throws Exception {
        mockMvc.perform(get("/api/literature/{id}/verify", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("Verified successfully!"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"literature.write.ACCESS"})
    public void testCreateNewLiterature() throws Exception {
        LiteratureSaveDTO saveDTO = new LiteratureSaveDTO(1L,"name",1L,"type","authoeName", "keywords", "description", "resourseLink");

        when(literatureMapper.fromLiteratureSaveDTOtoLiterature(any())).thenReturn(new Literature());

        mockMvc.perform(post("/api/literature")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(saveDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Successful save"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"literature.read.ACCESS"})
    public void testGetCourseForUpdate() throws Exception {
        Literature literature = new Literature(1L, "Sample Book");
        when(literatureService.getLiteratureById(1L)).thenReturn(literature);
        when(literatureMapper.fromLiteratureToCardDTO(any())).thenReturn(
                new LiteratureCardDTO(1L, "Sample Book"));

        mockMvc.perform(get("/api/literature/update/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Sample Book"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"literature.edit.ACCESS"})
    public void testEditLiterature() throws Exception {
        LiteratureSaveDTO saveDTO = new LiteratureSaveDTO(1L,"name",1L,"type","authoeName", "keywords", "description", "resourseLink");

        when(literatureMapper.fromLiteratureSaveDTOtoLiterature(any())).thenReturn(new Literature());

        mockMvc.perform(put("/api/literature/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(saveDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Successful update"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"literature.delete.ACCESS"})
    public void testDeleteLiterature() throws Exception {
        mockMvc.perform(delete("/api/literature/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"literature.read.ACCESS"})
    public void testGetLiteratureForSelect2() throws Exception {
        Page<Literature> literaturePage = new PageImpl<>(Arrays.asList(new Literature(1L, "Sample Book")));
        when(literatureService.getLiteratureByName(any(), any())).thenReturn(literaturePage);
        when(literatureMapper.fromPageLiteraturetoPageSelectList(any())).thenReturn(
                new PageImpl<>(Collections.singletonList(new LiteratureSelectDTO(1L, "Sample Book"))));

        mockMvc.perform(get("/api/literature/getLiteratures")
                        .param("searchQuery", "sample")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }


    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}