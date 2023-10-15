package com.example.spacelab.controller;


import com.example.spacelab.dto.task.TaskCardDTO;
import com.example.spacelab.exception.ErrorMessage;
import com.example.spacelab.exception.ObjectValidationException;
import com.example.spacelab.mapper.TaskMapper;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.lesson.LessonStatus;
import com.example.spacelab.model.role.PermissionType;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.dto.task.TaskInfoDTO;
import com.example.spacelab.dto.task.TaskSaveDTO;
import com.example.spacelab.dto.task.TaskListDTO;
import com.example.spacelab.model.task.TaskLevel;
import com.example.spacelab.model.task.TaskStatus;
import com.example.spacelab.service.TaskService;
import com.example.spacelab.util.AuthUtil;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.validator.TaskValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private final AuthUtil authUtil;


    // Получение списка задач (с фильтрами/страницами)
    @Operation(description = "Get list of tasks paginated by 'page/size' params (default values are 0/10), output depends on permission type(full/partial)", summary = "Get tasks list", tags = {"Task"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('tasks.read.NO_ACCESS')")
    @GetMapping
    public ResponseEntity<Page<TaskListDTO>> getTasks(@Parameter(name = "Filter object", description = "Collection of all filters for search results", example = "{}") FilterForm filters,
                                                      @RequestParam(required = false, defaultValue = "0") Integer page,
                                                      @RequestParam(required = false, defaultValue = "10") Integer size) {

        Page<TaskListDTO> tasks = new PageImpl<>(new ArrayList<>());
        Page<Task> taskPage;
        Pageable pageable = PageRequest.of(page, size);

        Admin loggedInAdmin = authUtil.getLoggedInAdmin();
        PermissionType permissionForLoggedInAdmin = loggedInAdmin.getRole().getPermissions().getReadStudents();
        List<Course> adminCourses = loggedInAdmin.getCourses();

        if(permissionForLoggedInAdmin == PermissionType.FULL) {
            taskPage = taskService.getTasks(filters, pageable);
            tasks = new PageImpl<>(taskPage.getContent().stream().map(mapper::fromTaskToListDTO).toList(), pageable, taskPage.getTotalElements());
        }
        else if(permissionForLoggedInAdmin == PermissionType.PARTIAL) {
            Long[] allowedCoursesIDs = (Long[]) adminCourses.stream().map(Course::getId).toArray();
            taskPage = taskService.getTasksByAllowedCourses(filters, pageable, allowedCoursesIDs);
            tasks = new PageImpl<>(taskPage.getContent().stream().map(mapper::fromTaskToListDTO).toList(), pageable, taskPage.getTotalElements());
        }

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }


    // Получение задачи по id
    @Operation(description = "Get task by id", summary = "Get task by id", tags = {"Task"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found in DB", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('tasks.read.NO_ACCESS')")
    @GetMapping("/{id}")
    public ResponseEntity<TaskInfoDTO> getTaskById(@PathVariable @Parameter(example = "1") Long id) {

        authUtil.checkAccessToCourse(taskService.getTaskById(id).getCourse().getId(), "tasks.read");

        TaskInfoDTO task = mapper.fromTaskToInfoDTO(taskService.getTaskById(id));
        return new ResponseEntity<>(task, HttpStatus.OK);
    }



    // Создание новой задачи
    @Operation(description = "Create new task; ID field does not matter in write/edit operations", summary = "Create new task", tags = {"Task"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful Creation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "400", description = "Task not valid", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('tasks.write.NO_ACCESS')")
    @PostMapping
    public ResponseEntity<String> createNewTask( @RequestBody TaskSaveDTO task, BindingResult bindingResult) {

        task.setId(null);

        authUtil.checkAccessToCourse(task.getCourseID(), "tasks.write");

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
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found in DB", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('tasks.read.NO_ACCESS')")
    @GetMapping("/edit/{id}")
    public ResponseEntity<TaskCardDTO> getTaskByIdForEdit(@PathVariable @Parameter(example = "1") Long id) {

        authUtil.checkAccessToCourse(taskService.getTaskById(id).getCourse().getId(), "tasks.read");

        TaskCardDTO task = mapper.fromTaskToCardDTO(taskService.getTaskById(id));
        return new ResponseEntity<>(task, HttpStatus.OK);
    }



    // Редактирование задачи
    @Operation(description = "Edit task; ID field does not matter in write/edit operations", summary = "Edit task", tags = {"Task"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Update"),
            @ApiResponse(responseCode = "400", description = "Bad Request / Validation Error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found in DB", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('tasks.edit.NO_ACCESS')")
    @PutMapping("/{id}")
    public ResponseEntity<String> editTask(@PathVariable @Parameter(example = "1") Long id,  @RequestBody TaskSaveDTO task, BindingResult bindingResult) {

        task.setId(id);

        authUtil.checkAccessToCourse(taskService.getTaskById(id).getCourse().getId(), "tasks.edit");
        authUtil.checkAccessToCourse(task.getCourseID(), "tasks.edit");

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
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found in DB", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('tasks.delete.NO_ACCESS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable @Parameter(example = "1") Long id) {

        authUtil.checkAccessToCourse(taskService.getTaskById(id).getCourse().getId(), "tasks.delete");

        taskService.deleteTaskById(id);
        return ResponseEntity.ok("Task with ID:"+id+" deleted");
    }

    // Получение списка уровней
    @GetMapping("/get-level-list")
    public List<TaskLevel> getLevelList() {
        return List.of(TaskLevel.values());
    }

    // Получение списка cтатусов
    @GetMapping("/get-status-list")
    public List<TaskStatus> getStatusList() {
        return List.of(TaskStatus.values());
    }

}
