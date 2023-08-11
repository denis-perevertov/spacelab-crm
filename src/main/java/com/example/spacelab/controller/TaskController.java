package com.example.spacelab.controller;


import com.example.spacelab.model.Task;
import com.example.spacelab.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Log
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    private List<Task> getTasks() {
        return new ArrayList<>();
    }

    @GetMapping("/{id}")
    private Task getTaskById(@PathVariable Long id) {
        return new Task();
    }

    @PostMapping
    private ResponseEntity<String> createNewTask(@RequestBody Task task) {

        return ResponseEntity.status(201).body("New task created");
    }

    @PutMapping
    private ResponseEntity<String> editTask(@RequestBody Task task) {

        return ResponseEntity.ok().build();
    }

    @PatchMapping
    private ResponseEntity<String> editTaskPatch() {

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<String> deleteTask() {

        return ResponseEntity.ok().build();
    }


}
