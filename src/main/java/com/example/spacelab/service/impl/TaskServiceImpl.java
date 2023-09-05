package com.example.spacelab.service.impl;

import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.mapper.TaskMapper;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.repository.TaskRepository;
import com.example.spacelab.service.TaskService;
import com.example.spacelab.service.specification.TaskSpecifications;
import com.example.spacelab.util.FilterForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper mapper;

    @Override
    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Page<Task> getTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    @Override
    public Page<Task> getTasks(FilterForm filters, Pageable pageable) {
        Specification<Task> spec = buildSpecificationFromFilters(filters);
        return taskRepository.findAll(spec, pageable);
    }

    @Override
    public List<Task> getTasksByAllowedCourses(Long... ids) {
        return taskRepository.findAllByAllowedCourse(ids);
    }

    @Override
    public Page<Task> getTasksByAllowedCourses(Pageable pageable, Long... ids) {
        return taskRepository.findAllByAllowedCoursePage(pageable, ids);
    }

    @Override
    public Page<Task> getTasksByAllowedCourses(FilterForm filters, Pageable pageable, Long... ids) {
        Specification<Task> spec = buildSpecificationFromFilters(filters).and(TaskSpecifications.hasCourseIDs(ids));
        return taskRepository.findAll(spec, pageable);
    }

    @Override
    public Task getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        return task;
    }

    @Override
    public Task createNewTask(Task taskIn) {
        Task task = taskRepository.save(taskIn);
        return task;
    }

    @Override
    public Task editTask(Task taskIn) {
        Task task = taskRepository.save(taskIn);
        return task;
    }

    @Override
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public Specification<Task> buildSpecificationFromFilters(FilterForm filters) {
        return null;
    }


}
