package com.example.spacelab.controller;


import com.example.spacelab.dto.TaskDTO;
import com.example.spacelab.service.TaskService;
import com.example.spacelab.util.FilterForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    private ResponseEntity<List<TaskDTO>> getTasks(FilterForm filters,
                                                   @RequestParam Integer page,
                                                   @RequestParam(required = false) Integer size) {

        List<TaskDTO> taskList = taskService.getTasks(filters, PageRequest.of(page, (size == null) ? 10 : size));
        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    private ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        TaskDTO task = taskService.getTaskById(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PostMapping
    private ResponseEntity<TaskDTO> createNewTask(@Valid @RequestBody TaskDTO task) {
        TaskDTO newTask = taskService.createNewTask(task);
        return new ResponseEntity<>(newTask, HttpStatus.CREATED);
    }

    @PutMapping
    private ResponseEntity<TaskDTO> editTask(@Valid @RequestBody TaskDTO task) {
        TaskDTO editedTask = taskService.editTask(task);
        return new ResponseEntity<>(editedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<String> deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.ok("Task with ID:"+id+" deleted");
    }


}
