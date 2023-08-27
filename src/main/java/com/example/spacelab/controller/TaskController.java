package com.example.spacelab.controller;


import com.example.spacelab.mapper.TaskMapper;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.dto.task.TaskInfoDTO;
import com.example.spacelab.dto.task.TaskSaveDTO;
import com.example.spacelab.dto.task.TaskListDTO;
import com.example.spacelab.service.TaskService;
import com.example.spacelab.util.FilterForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper mapper;

    @GetMapping
    private ResponseEntity<Page<TaskListDTO>> getTasks(FilterForm filters,
                                                       @RequestParam Integer page,
                                                       @RequestParam(required = false) Integer size) {
        Page<Task> taskList = taskService.getTasks(filters, PageRequest.of(page, (size == null) ? 10 : size));
        Page<TaskListDTO> taskListDTO = mapper.fromTaskPageToDTOPage(taskList);
        return new ResponseEntity<>(taskListDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    private ResponseEntity<TaskInfoDTO> getTaskById(@PathVariable Long id) {
        TaskInfoDTO task = mapper.fromTaskToInfoDTO(taskService.getTaskById(id));
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PostMapping
    private ResponseEntity<String> createNewTask(@Valid @RequestBody TaskSaveDTO task) {

        Task newTask = taskService.createNewTask(mapper.fromTaskSaveDTOToTask(task));
        return ResponseEntity.ok("Task with ID:"+newTask.getId()+" created");
    }

    @PutMapping
    private ResponseEntity<String> editTask(@Valid @RequestBody TaskSaveDTO task) {
        Task newTask = taskService.createNewTask(mapper.fromTaskSaveDTOToTask(task));
        return ResponseEntity.ok("Task with ID:"+newTask.getId()+" updated");

    }

    @DeleteMapping("/{id}")
    private ResponseEntity<String> deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.ok("Task with ID:"+id+" deleted");
    }


}
