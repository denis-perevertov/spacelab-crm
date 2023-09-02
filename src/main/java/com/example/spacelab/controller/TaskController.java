package com.example.spacelab.controller;


import com.example.spacelab.dto.task.TaskCardDTO;
import com.example.spacelab.exception.ErrorMessage;
import com.example.spacelab.exception.ObjectValidationException;
import com.example.spacelab.mapper.TaskMapper;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.role.PermissionType;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.dto.task.TaskInfoDTO;
import com.example.spacelab.dto.task.TaskSaveDTO;
import com.example.spacelab.dto.task.TaskListDTO;
import com.example.spacelab.service.TaskService;
import com.example.spacelab.util.AuthUtil;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.validator.TaskValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Tag(name="Task", description = "Task controller")
@RestController
@Log
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper mapper;
    private final TaskValidator taskValidator;


    // Получение списка задач (с фильтрами/страницами)
    @Operation(description = "Get tasks list", summary = "Get tasks list", tags = {"Task"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('tasks.read.NO_ACCESS')")
    @GetMapping
    private ResponseEntity<Page<TaskListDTO>> getTasks(FilterForm filters,
                                                       @RequestParam(required = false) Integer page,
                                                       @RequestParam(required = false) Integer size) {

        Page<TaskListDTO> taskListDTO = new PageImpl<>(new ArrayList<>());

        Admin loggedInAdmin = AuthUtil.getLoggedInAdmin();
        PermissionType permissionForLoggedInAdmin = loggedInAdmin.getRole().getPermissions().getReadTasks();

        if(permissionForLoggedInAdmin == PermissionType.FULL) {
            if(page == null && size == null) taskListDTO = new PageImpl<>(taskService.getTasks().stream().map(mapper::fromTaskToListDTO).toList());
            else if(page != null && size == null) taskListDTO = new PageImpl<>(taskService.getTasks(PageRequest.of(page, 10)).stream().map(mapper::fromTaskToListDTO).toList());
            else taskListDTO = new PageImpl<>(taskService.getTasks(PageRequest.of(page, size)).stream().map(mapper::fromTaskToListDTO).toList());
        }
        else if(permissionForLoggedInAdmin == PermissionType.PARTIAL) {

            Long[] allowedCoursesIDs = (Long[]) loggedInAdmin.getCourses().stream().map(Course::getId).toArray();

            if(page == null && size == null) taskListDTO = new PageImpl<>(taskService.getTasksByAllowedCourses(allowedCoursesIDs).stream().map(mapper::fromTaskToListDTO).toList());
            else if(page != null && size == null) taskListDTO = new PageImpl<>(taskService.getTasksByAllowedCourses(PageRequest.of(page, 10), allowedCoursesIDs).stream().map(mapper::fromTaskToListDTO).toList());
            else taskListDTO = new PageImpl<>(taskService.getTasksByAllowedCourses(filters, PageRequest.of(page, size), allowedCoursesIDs).stream().map(mapper::fromTaskToListDTO).toList());

        }

        return new ResponseEntity<>(taskListDTO, HttpStatus.OK);
    }


    // Получение задачи по id
    @Operation(description = "Get task by id", summary = "Get task by id", tags = {"Task"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Task not found in DB", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('tasks.read.NO_ACCESS')")
    @GetMapping("/{id}")
    private ResponseEntity<TaskInfoDTO> getTaskById(@PathVariable Long id) {

        AuthUtil.checkAccessToCourse(taskService.getTaskById(id).getCourse().getId(), "tasks.read");

        TaskInfoDTO task = mapper.fromTaskToInfoDTO(taskService.getTaskById(id));
        return new ResponseEntity<>(task, HttpStatus.OK);
    }



    // Создание новой задачи
    @Operation(description = "Create new task", summary = "Create new task", tags = {"Task"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Task not valid", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('tasks.write.NO_ACCESS')")
    @PostMapping
    private ResponseEntity<String> createNewTask( @RequestBody TaskSaveDTO task, BindingResult bindingResult) {

        AuthUtil.checkAccessToCourse(task.getCourseId(), "tasks.write");

        taskValidator.validate(task, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ObjectValidationException(errors);
        }
        Task newTask = taskService.createNewTask(mapper.fromTaskSaveDTOToTask(task));
        return ResponseEntity.ok("Task with ID:"+newTask.getId()+" created");
    }



    // Получение задачи для редактирования по id
    @Operation(description = "Get task by id for edit", summary = "Get task by id for edit", tags = {"Task"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Task not found in DB", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('tasks.read.NO_ACCESS')")
    @GetMapping("/edit/{id}")
    private ResponseEntity<TaskCardDTO> getTaskByIdForEdit(@PathVariable Long id) {

        AuthUtil.checkAccessToCourse(taskService.getTaskById(id).getCourse().getId(), "tasks.read");

        TaskCardDTO task = mapper.fromTaskToCardDTO(taskService.getTaskById(id));
        return new ResponseEntity<>(task, HttpStatus.OK);
    }



    // Редактирование задачи
    @Operation(description = "Edit task", summary = "Edit task", tags = {"Task"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Task not valid", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "404", description = "Task not found in DB", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('tasks.edit.NO_ACCESS')")
    @PutMapping("/{id}")
    private ResponseEntity<String> editTask(@PathVariable Long id,  @RequestBody TaskSaveDTO task, BindingResult bindingResult) {

        AuthUtil.checkAccessToCourse(taskService.getTaskById(id).getCourse().getId(), "tasks.edit");
        AuthUtil.checkAccessToCourse(task.getCourseId(), "tasks.edit");

        taskValidator.validate(task, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ObjectValidationException(errors);
        }
        Task newTask = taskService.createNewTask(mapper.fromTaskSaveDTOToTask(task));
        return ResponseEntity.ok("Task with ID:"+newTask.getId()+" updated");

    }



    // Удаление задачи
    @Operation(description = "Delete task", summary = "Delete task", tags = {"Task"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Task not found in DB", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('tasks.delete.NO_ACCESS')")
    @DeleteMapping("/{id}")
    private ResponseEntity<String> deleteTask(@PathVariable Long id) {

        AuthUtil.checkAccessToCourse(taskService.getTaskById(id).getCourse().getId(), "tasks.delete");

        taskService.deleteTaskById(id);
        return ResponseEntity.ok("Task with ID:"+id+" deleted");
    }

}
