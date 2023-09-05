package com.example.spacelab.controller;

import com.example.spacelab.config.SecurityConfig;
import com.example.spacelab.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import com.example.spacelab.model.lesson.Lesson;
import com.example.spacelab.dto.lesson.LessonListDTO;
import com.example.spacelab.dto.lesson.LessonInfoDTO;
import com.example.spacelab.dto.lesson.LessonSaveBeforeStartDTO;
import com.example.spacelab.validator.LessonBeforeStartValidator;
import com.example.spacelab.service.LessonService;
import com.example.spacelab.mapper.LessonMapper;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.exception.ErrorMessage;
import com.example.spacelab.exception.ObjectValidationException;
import com.example.spacelab.model.lesson.LessonStatus;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.service.LessonReportRowService;
import com.example.spacelab.model.lesson.LessonReport;
import com.example.spacelab.dto.lesson.LessonReportRowSaveDTO;
import com.example.spacelab.dto.lesson.LessonReportRowDTO;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@WithUserDetails("test@gmail.com")
class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonService lessonService;

    @MockBean
    private LessonMapper lessonMapper;

    @MockBean
    private LessonBeforeStartValidator lessonBeforeStartValidator;

    @Test
    public void testGetLesson() throws Exception {

        Admin user = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user.toString());
        System.out.println(user.getRole().toString());

        // Arrange
        FilterForm filters = new FilterForm();
        Page<Lesson> lessonPage = new PageImpl<>(Arrays.asList(new Lesson(1L, LocalDateTime.now())));
        Page<LessonListDTO> lessonListDTOPage = new PageImpl<>(Collections.singletonList(new LessonListDTO(1L, LocalDateTime.now())));

        when(lessonService.getLesson(any(), any())).thenReturn(lessonPage);
        when(lessonMapper.pageLessonToPageLessonListDTO(any())).thenReturn(lessonListDTOPage);

        MvcResult result = mockMvc.perform(get("/api/lessons")).andReturn();
        System.out.println(result.getResponse().getContentAsString());

        // Act and Assert
        mockMvc.perform(get("/api/lessons?page=0&size=10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].datetime").isNotEmpty());
    }

    @Test
    public void testGetLessonById() throws Exception {
        // Arrange
        Long lessonId = 1L;
        LessonInfoDTO lessonInfoDTO = new LessonInfoDTO(1L, LocalDateTime.now(), "Status");

        when(lessonService.getLessonById(lessonId)).thenReturn(new Lesson(1L, LocalDateTime.now()));
        when(lessonMapper.fromLessonToLessonInfoDTO(any())).thenReturn(lessonInfoDTO);

        // Act and Assert
        mockMvc.perform(get("/api/lessons/{id}", lessonId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.datetime").isNotEmpty())
                .andExpect(jsonPath("$.status").value("Status"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"lesson.write.ACCESS"})
    public void testCreateNewLessonBeforeStart() throws Exception {
        // Arrange
        LessonSaveBeforeStartDTO saveBeforeStartDTO = new LessonSaveBeforeStartDTO();

        when(lessonMapper.BeforeStartDTOtoLesson(any())).thenReturn(new Lesson());

        // Act and Assert
        mockMvc.perform(post("/api/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(saveBeforeStartDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Successful save"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"lesson.edit.ACCESS"})
    public void testEditLessonBeforeStart() throws Exception {
        // Arrange
        Long lessonId = 1L;
        LessonSaveBeforeStartDTO saveBeforeStartDTO = new LessonSaveBeforeStartDTO();

        when(lessonMapper.BeforeStartDTOtoLesson(any())).thenReturn(new Lesson());

        // Act and Assert
        mockMvc.perform(put("/api/lessons/{id}", lessonId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(saveBeforeStartDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Successful update"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"lesson.delete.ACCESS"})
    public void testDeleteLesson() throws Exception {
        // Arrange
        Long lessonId = 1L;

        // Act and Assert
        mockMvc.perform(delete("/api/lessons/{id}", lessonId))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted"));
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}