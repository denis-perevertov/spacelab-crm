package com.example.spacelab.service;

import com.example.spacelab.model.Task;
import com.example.spacelab.model.dto.TaskDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    List<Task> getAllTasks();
    List<TaskDTO> getTasksByPage(Pageable pageable);
    Task getTaskById(Long id);
    TaskDTO getTaskDTOById(Long id);
    Task createNewTask(Task task);
    Task createNewTask(TaskDTO task);
    Task editTask(Task task);
    Task editTask(TaskDTO task);
    void deleteTaskById(Long id);

}
