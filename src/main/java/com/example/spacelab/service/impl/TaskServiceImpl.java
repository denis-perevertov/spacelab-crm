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

    @Override
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream().map(mapper::fromTaskToDTO).toList();
    }

    @Override
    public List<TaskDTO> getTasksByPage(Pageable pageable) {
        return taskRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(mapper::fromTaskToDTO)
                .toList();
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        return mapper.fromTaskToDTO(task);
    }

    @Override
    public TaskDTO getTaskDTOById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        return mapper.fromTaskToDTO(task);
    }

    @Override
    public TaskDTO createNewTask(TaskDTO dto) {
        Task task = mapper.fromDTOToTask(dto);
        task = taskRepository.save(task);
        return mapper.fromTaskToDTO(task);
    }

    @Override
    public TaskDTO editTask(TaskDTO dto) {
        Task task = mapper.fromDTOToTask(dto);
        task = taskRepository.save(task);
        return mapper.fromTaskToDTO(task);
    }

    @Override
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }
}
