package com.example.spacelab.service;

import com.example.spacelab.model.Task;
import com.example.spacelab.model.dto.TaskDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    List<TaskDTO> getAllTasks();
    List<TaskDTO> getTasksByPage(Pageable pageable);
    TaskDTO getTaskById(Long id);
    TaskDTO getTaskDTOById(Long id);
    TaskDTO createNewTask(TaskDTO task);
    TaskDTO editTask(TaskDTO task);
    void deleteTaskById(Long id);

}
