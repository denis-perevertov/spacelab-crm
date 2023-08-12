package com.example.spacelab.service.impl;

import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.mapper.TaskDTOMapper;
import com.example.spacelab.model.Task;
import com.example.spacelab.model.dto.TaskDTO;
import com.example.spacelab.repository.TaskRepository;
import com.example.spacelab.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskDTOMapper mapper;
//    private final TaskMapper mapper;
//    private final TaskMapper mapper = Mappers.getMapper(TaskMapper.class);

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

//    @Override
//    public Page<TaskDTO> getTasksByPage(Pageable pageable) {
//        return taskRepository.findAll(pageable);
//    }

    @Override
    public List<TaskDTO> getTasksByPage(Pageable pageable) {
        return taskRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(mapper::fromTaskToDTO)
                .toList();
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    @Override
    public TaskDTO getTaskDTOById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        return mapper.fromTaskToDTO(task);
    }

    @Override
    public Task createNewTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task createNewTask(TaskDTO task) {
        return taskRepository.save(mapper.fromDTOToTask(task));
    }

    @Override
    public Task editTask(Task task) {
        return null;
    }

    @Override
    public Task editTask(TaskDTO task) {
        return null;
    }

    @Override
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }
}
