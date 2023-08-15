package com.example.spacelab.controller;


import com.example.spacelab.model.dto.TaskDTO;
import com.example.spacelab.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
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
    private List<TaskDTO> getTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    private TaskDTO getTaskById(@PathVariable Long id) {
        return taskService.getTaskDTOById(id);
    }

    @PostMapping
    private ResponseEntity<TaskDTO> createNewTask(@RequestBody TaskDTO task) {
        TaskDTO newTask = taskService.createNewTask(task);
        return ResponseEntity.status(201).body(newTask);
    }

    @PutMapping
    private ResponseEntity<TaskDTO> editTask(@RequestBody TaskDTO task) {
        TaskDTO editedTask = taskService.createNewTask(task);
        return ResponseEntity.status(200).body(editedTask);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<String> deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.ok("Task with ID:"+id+" deleted");
    }


}
