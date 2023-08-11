package com.example.spacelab.service;

import com.example.spacelab.model.Task;

import java.util.List;

public interface TaskService {

    List<Task> getTasks();
    Task getTaskById(Long id);
    Task createNewTask(Task task);
    Task editTask(Task task);
    void deleteTaskById(Long id);

}
