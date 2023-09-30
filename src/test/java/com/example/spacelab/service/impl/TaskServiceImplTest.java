//package com.example.spacelab.service.impl;
//
//import com.example.spacelab.exception.ResourceNotFoundException;
//import com.example.spacelab.mapper.TaskMapper;
//import com.example.spacelab.model.task.Task;
//import com.example.spacelab.repository.TaskRepository;
//import com.example.spacelab.util.FilterForm;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.mockito.internal.verification.VerificationModeFactory.times;
//
//@SpringBootTest
//@SpringJUnitConfig
//@AutoConfigureMockMvc
//class TaskServiceImplTest {
//
//
//    @Mock
//    private TaskRepository taskRepository;
//
//    @Mock
//    private TaskMapper taskMapper;
//
//    private TaskServiceImpl taskService;
//
//    @BeforeEach
//    void setUp() {
//        taskService = new TaskServiceImpl(taskRepository, taskMapper);
//    }
//
//    @Test
//    public void testGetTasks() {
//        List<Task> mockTaskList = new ArrayList<>();
//        mockTaskList.add(new Task());
//        mockTaskList.add(new Task());
//        when(taskRepository.findAll()).thenReturn(mockTaskList);
//
//        List<Task> result = taskService.getTasks();
//
//        assertEquals(2, result.size());
//    }
//
//    @Test
//    public void testGetTaskById() {
//        Long taskId = 1L;
//        Task mockTask = new Task();
//        mockTask.setId(taskId);
//        when(taskRepository.findById(taskId)).thenReturn(Optional.of(mockTask));
//
//        Task result = taskService.getTaskById(taskId);
//
//        assertEquals(taskId, result.getId());
//    }
//
//    @Test
//    public void testGetTaskById_ThrowsException() {
//        Long taskId = 1L;
//        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () -> taskService.getTaskById(taskId));
//    }
//
//    @Test
//    public void testCreateNewTask() {
//        Task mockTask = new Task();
//        when(taskRepository.save(any(Task.class))).thenReturn(mockTask);
//
//        Task result = taskService.createNewTask(mockTask);
//
//        assertEquals(mockTask, result);
//    }
//
//    @Test
//    public void testEditTask() {
//        Task mockTask = new Task();
//        when(taskRepository.save(any(Task.class))).thenReturn(mockTask);
//
//        Task result = taskService.editTask(mockTask);
//
//        assertEquals(mockTask, result);
//    }
//
//    @Test
//    public void testDeleteTaskById() {
//        Long taskId = 1L;
//
//        taskService.deleteTaskById(taskId);
//
//        verify(taskRepository, times(1)).deleteById(taskId);
//    }
//
//    @Test
//    public void testBuildSpecificationFromFilters() {
//        FilterForm filters = new FilterForm(); // Передайте фильтры по вашей логике
//
//        Specification<Task> result = taskService.buildSpecificationFromFilters(filters);
//
//        // Здесь вы можете добавить утверждения, связанные с созданным Specification.
//    }
//}