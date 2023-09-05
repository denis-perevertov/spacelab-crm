package com.example.spacelab.controller;

import com.example.spacelab.config.SecurityConfig;
import com.example.spacelab.dto.task.TaskCardDTO;
import com.example.spacelab.dto.task.TaskInfoDTO;
import com.example.spacelab.dto.task.TaskListDTO;
import com.example.spacelab.dto.task.TaskSaveDTO;
import com.example.spacelab.mapper.TaskMapper;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.service.TaskService;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.validator.TaskValidator;
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
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private TaskMapper taskMapper;

    @MockBean
    private TaskValidator taskValidator;

    @Test
    @WithMockUser(username = "user", authorities = {"task.read.ACCESS"})
    public void testGetTasks() throws Exception {
        // Arrange
        FilterForm filters = new FilterForm();
        Page<Task> taskPage = new PageImpl<>(Arrays.asList(new Task(1L, "Sample Task")));
        Page<TaskListDTO> taskListDTOPage = new PageImpl<>(Collections.singletonList(new TaskListDTO(1L, "Sample Task")));

        when(taskService.getTasks(any(), any())).thenReturn(taskPage);
        when(taskMapper.fromTaskPageToDTOPage(any())).thenReturn(taskListDTOPage);

        // Act and Assert
        mockMvc.perform(get("/api/tasks")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Sample Task"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"task.read.ACCESS"})
    public void testGetTaskById() throws Exception {
        // Arrange
        Long taskId = 1L;
        TaskInfoDTO taskInfoDTO = new TaskInfoDTO(1L, "Sample Task");

        when(taskService.getTaskById(taskId)).thenReturn(new Task(1L, "Sample Task"));
        when(taskMapper.fromTaskToInfoDTO(any())).thenReturn(taskInfoDTO);

        // Act and Assert
        mockMvc.perform(get("/api/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Sample Task"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"task.write.ACCESS"})
    public void testCreateNewTask() throws Exception {
        // Arrange
        TaskSaveDTO saveDTO = new TaskSaveDTO();

        when(taskMapper.fromTaskSaveDTOToTask(any())).thenReturn(new Task());
        Task newTask = new Task(1L,"name");
        when(taskService.createNewTask(any())).thenReturn(newTask);
        // Act and Assert
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(saveDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Task with ID:"+newTask.getId()+" created"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"task.read.ACCESS"})
    public void testGetTaskByIdForEdit() throws Exception {
        // Arrange
        Long taskId = 1L;
        TaskCardDTO taskCardDTO = new TaskCardDTO(1L, "Sample Task");

        when(taskService.getTaskById(taskId)).thenReturn(new Task(1L, "Sample Task"));
        when(taskMapper.fromTaskToCardDTO(any())).thenReturn(taskCardDTO);

        // Act and Assert
        mockMvc.perform(get("/api/tasks/edit/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Sample Task"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"task.edit.ACCESS"})
    public void testEditTask() throws Exception {
        // Arrange
        TaskSaveDTO saveDTO = new TaskSaveDTO();

        when(taskMapper.fromTaskSaveDTOToTask(any())).thenReturn(new Task());
        Task newTask = new Task(1L,"name");
        when(taskService.createNewTask(any())).thenReturn(newTask);

        mockMvc.perform(put("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(saveDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Task with ID:"+newTask.getId()+" updated"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"task.delete.ACCESS"})
    public void testDeleteTask() throws Exception {
        // Arrange
        Long taskId = 1L;

        // Act and Assert
        mockMvc.perform(delete("/api/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(content().string("Task with ID:" + taskId + " deleted"));
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}