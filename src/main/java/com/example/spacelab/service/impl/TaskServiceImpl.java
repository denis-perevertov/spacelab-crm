package com.example.spacelab.service.impl;

import com.example.spacelab.model.Task;
import com.example.spacelab.repository.TaskRepository;
import com.example.spacelab.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public List<Task> getTasks() {
        return null;
    }

    @Override
    public Task getTaskById(Long id) {
        return null;
    }

    @Override
    public Task createNewTask(Task task) {
        return null;
    }

    @Override
    public Task editTask(Task task) {
        return null;
    }

    @Override
    public void deleteTaskById(Long id) {

    }
}
