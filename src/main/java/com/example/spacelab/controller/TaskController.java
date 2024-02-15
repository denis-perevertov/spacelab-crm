package com.example.spacelab.controller;


import com.example.spacelab.dto.SelectDTO;
import com.example.spacelab.dto.course.CourseSelectDTO;
import com.example.spacelab.dto.task.*;
import com.example.spacelab.exception.ErrorMessage;
import com.example.spacelab.exception.ObjectValidationException;
import com.example.spacelab.mapper.StudentMapper;
import com.example.spacelab.mapper.TaskMapper;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.lesson.LessonStatus;
import com.example.spacelab.model.literature.LiteratureType;
import com.example.spacelab.model.role.PermissionType;
import com.example.spacelab.model.task.Task;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;

@Tag(name="Task", description = "Task controller")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper mapper;
    private final StudentMapper studentMapper;
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
    public ResponseEntity<?> getTasks(@Parameter(name = "Filter object", description = "Collection of all filters for search results", example = "{}") FilterForm filters,
                                                      @RequestParam(required = false, defaultValue = "0") Integer page,
                                                      @RequestParam(required = false, defaultValue = "10") Integer size) {

        Page<TaskListDTO> tasks = new PageImpl<>(new ArrayList<>());
        Page<Task> taskPage;
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");

        Admin loggedInAdmin = authUtil.getLoggedInAdmin();
        PermissionType permissionForLoggedInAdmin = loggedInAdmin.getRole().getPermissions().getReadStudents();
        Set<Course> adminCourses = loggedInAdmin.getCourses();

        if(permissionForLoggedInAdmin == PermissionType.FULL) {
            taskPage = taskService.getTasks(filters.trim(), pageable);
            tasks = new PageImpl<>(taskPage.getContent().stream().map(mapper::fromTaskToListDTO).toList(), pageable, taskPage.getTotalElements());
        }
        else if(permissionForLoggedInAdmin == PermissionType.PARTIAL) {
            Long[] allowedCoursesIDs = adminCourses.stream().map(Course::getId).toArray(Long[]::new);
            taskPage = taskService.getTasksByAllowedCourses(filters.trim(), pageable, allowedCoursesIDs);
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
    public ResponseEntity<?> getTaskById(@PathVariable @Parameter(example = "1") Long id) {

        Task t = taskService.getTaskById(id);
        if(t.getCourse() != null) {
            authUtil.checkAccessToCourse(t.getCourse().getId(), "tasks.read");
        }

        TaskInfoDTO task = mapper.fromTaskToInfoDTO(taskService.getTaskById(id));
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<?> getTaskStudentsById(@PathVariable Long id) {

        Task t = taskService.getTaskById(id);
        if(t.getCourse() != null) {
            authUtil.checkAccessToCourse(t.getCourse().getId(), "tasks.read");
        }

        return ResponseEntity.ok(studentMapper.fromStudentListToAvatarListDTO(taskService.getTaskStudents(id)));
    }

    @GetMapping("/{id}/course")
    public ResponseEntity<?> getTaskCourseById(@PathVariable Long id) {
        Task t = taskService.getTaskById(id);
        if(t.getCourse() != null) {
            authUtil.checkAccessToCourse(t.getCourse().getId(), "tasks.read");
            return ResponseEntity.ok(new CourseSelectDTO(t.getCourse().getId(), t.getCourse().getName()));
        }
        else return ResponseEntity.notFound().build();
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
    public ResponseEntity<?> createNewTask( @RequestBody TaskSaveDTO task, BindingResult bindingResult) {

        task.setId(null);

        if(task.getCourseID() != null) {
            authUtil.checkAccessToCourse(task.getCourseID(), "tasks.write");
        }

        taskValidator.validate(task, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ObjectValidationException(errors);
        }
        Task newTask = taskService.createNewTask(mapper.fromTaskSaveDTOToTask(task));
        return ResponseEntity.ok(mapper.fromTaskToListDTO(newTask));
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
    public ResponseEntity<?> getTaskByIdForEdit(@PathVariable @Parameter(example = "1") Long id) {

        Task t = taskService.getTaskById(id);
        if(t.getCourse() != null) {
            authUtil.checkAccessToCourse(t.getCourse().getId(), "tasks.read");
        }

        TaskSaveDTO task = mapper.fromTaskToSaveDTO(taskService.getTaskById(id));
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
    public ResponseEntity<?> editTask(@PathVariable @Parameter(example = "1") Long id,  @RequestBody TaskSaveDTO task, BindingResult bindingResult) {

        task.setId(id);
        Task t = taskService.getTaskById(id);
        Course c = t.getCourse();
        // check access to editing tasks of current course
        if(c != null) {
            authUtil.checkAccessToCourse(c.getId(), "tasks.edit");
        }
        // check access to editing tasks of new course
        if(c != null && !c.getId().equals(task.getCourseID())) {
            authUtil.checkAccessToCourse(task.getCourseID(), "tasks.edit");
        }

        taskValidator.validate(task, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ObjectValidationException(errors);
        }
        Task editedTask = taskService.editTask(mapper.fromTaskSaveDTOToTask(task));
        return ResponseEntity.ok(mapper.fromTaskToListDTO(editedTask));

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
    public ResponseEntity<?> deleteTask(@PathVariable @Parameter(example = "1") Long id) {

        Task task = taskService.getTaskById(id);
        if(task.getCourse() != null) {
            authUtil.checkAccessToCourse(task.getCourse().getId(), "tasks.delete");
        }

        taskService.deleteTaskById(id);
        return ResponseEntity.ok("Task with ID:"+id+" deleted");
    }

    // Экспорт в PDF
    @GetMapping("/{id}/export/pdf")
    public ResponseEntity<?> exportTaskToPDF(@PathVariable Long id,
                                             @RequestParam(required = false, defaultValue = "ua") String locale) throws IOException {
        File file;
        try {
            file = taskService.generatePDF(id, locale);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.unprocessableEntity().body("Could not generate pdf file");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData(file.getName(), file.getName());
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(
                new InputStreamResource(Files.newInputStream(file.getAbsoluteFile().toPath(), StandardOpenOption.DELETE_ON_CLOSE)),
                headers,
                HttpStatus.OK
        );
    }

    // Получение подзадач для какой-то 1 задачи
    @GetMapping("/{id}/subtasks")
    public ResponseEntity<?> getSubtasks(@PathVariable Long id) {

        Task task = taskService.getTaskById(id);
        if(task.getCourse() != null) {
            authUtil.checkAccessToCourse(task.getCourse().getId(), "tasks.read");
        }

        List<Task> subtasks = taskService.getTaskSubtasks(id);
        return ResponseEntity.ok(mapper.fromSubtaskToDTOList(subtasks));
    }

    @PostMapping("/{taskId}/subtasks/add")
    public ResponseEntity<?> addSubtaskToList(@PathVariable Long taskId,
                                              @RequestBody TaskSubtaskListDTO dto) {
        List<Task> subtasks = taskService.addSubtasksToTask(taskId, dto);
        return ResponseEntity.ok(mapper.fromSubtaskToDTOList(subtasks));
    }

    // перемешивание подзадач
    @PostMapping("/{taskId}/subtasks/shuffle")
    public ResponseEntity<?> shuffleSubtasks(@PathVariable Long taskId,
                                             @RequestBody SubtaskShuffleRequest request) {
        taskService.shuffleSubtasks(request);
        return ResponseEntity.ok().build();
    }

    // Удаление подзадачи из списка (не удаление задания целиком)
    @DeleteMapping("/{taskId}/subtasks/remove/{subtaskId}")
    public ResponseEntity<?> removeSubtaskFromList(@PathVariable Long taskId,
                                                   @PathVariable Long subtaskId) {
        taskService.removeSubtaskFromList(taskId, subtaskId);
        return ResponseEntity.accepted().build();
    }

    // Получение задач(родительских) без курса
    @GetMapping("/unused")
    public ResponseEntity<?> getTasksWithoutCourse(FilterForm filters,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> availableTasks = taskService.getTasksWithoutCourse(taskService.buildSpecificationFromFilters(filters.trim()), pageable);
        return ResponseEntity.ok(
                new PageImpl<>(
                        availableTasks.stream().map(mapper::fromTaskToModalDTO).toList(),
                        pageable,
                        availableTasks.getTotalElements()
                )
        );
    }

    // Получение задач(родительских) , сортировка по наличию курса
    @GetMapping("/parent")
    public ResponseEntity<?> getAvailableTasks(FilterForm filters,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> availableTasks = taskService.getParentTasks(taskService.buildSpecificationFromFilters(filters.trim()), pageable);
        return ResponseEntity.ok(
                new PageImpl<>(
                        availableTasks.stream().map(mapper::fromTaskToModalDTO).toList(),
                        pageable,
                        availableTasks.getTotalElements()
                )
        );
    }

    // Получение списка уровней
    @GetMapping("/get-level-list")
    public List<SelectDTO> getLevelList() {
        return Arrays.stream(TaskLevel.values()).map(type -> new SelectDTO(type.name(), type.name())).toList();
    }

    // Получение списка cтатусов
    @GetMapping("/get-status-list")
    public List<SelectDTO> getStatusList() {
        return Arrays.stream(TaskStatus.values()).map(type -> new SelectDTO(type.name(), type.name())).toList();
    }

}
