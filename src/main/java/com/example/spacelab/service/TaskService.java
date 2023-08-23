package com.example.spacelab.service;

import com.example.spacelab.model.Task;
import com.example.spacelab.model.dto.TaskDTO.TaskListDTO;
import com.example.spacelab.util.FilterForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService extends EntityFilterService<Task>{

    List<Task> getTasks();
    Page<Task> getTasks(Pageable pageable);
    Page<Task> getTasks(FilterForm filters, Pageable pageable);
    Task getTaskById(Long id);
    Task createNewTask(Task task);
    Task editTask(Task task);
    void deleteTaskById(Long id);


//    List<TaskListDTO> getTasks();
//    List<TaskListDTO> getTasks(Pageable pageable);
//    List<TaskListDTO> getTasks(FilterForm filters, Pageable pageable);
//    TaskListDTO getTaskById(Long id);
//    TaskListDTO createNewTask(TaskListDTO task);
//    TaskListDTO editTask(TaskListDTO task);
//    void deleteTaskById(Long id);

}
