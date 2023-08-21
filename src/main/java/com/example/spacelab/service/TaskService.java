package com.example.spacelab.service;

import com.example.spacelab.model.Task;
import com.example.spacelab.model.dto.TaskDTO;
import com.example.spacelab.util.FilterForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService extends EntityFilterService<Task>{

    List<TaskDTO> getTasks();
    List<TaskDTO> getTasks(Pageable pageable);
    List<TaskDTO> getTasks(FilterForm filters, Pageable pageable);
    TaskDTO getTaskById(Long id);
    TaskDTO createNewTask(TaskDTO task);
    TaskDTO editTask(TaskDTO task);
    void deleteTaskById(Long id);

}
