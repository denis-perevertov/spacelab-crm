package com.example.spacelab.api;

import com.example.spacelab.dto.task.SubtaskShuffleRequest;
import com.example.spacelab.dto.task.TaskSaveDTO;
import com.example.spacelab.dto.task.TaskSubtaskListDTO;
import com.example.spacelab.util.FilterForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

public interface TaskAPI {

    /**
     * Get list of tasks paginated by 'page/size' params (default values are 0/10), output depends on permission type(full/partial)
     * @param filters {@link FilterForm}, filter object
     * @param page {@link Integer}, default value 0
     * @param size {@link Integer}, default value 10
     * @return
     */
    @Operation(description = "Get list of tasks paginated by 'page/size' params (default values are 0/10), output depends on permission type(full/partial)", summary = "Get tasks list", tags = {"Task"})
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('tasks.read.NO_ACCESS')")
    ResponseEntity<?> getTasks(@Parameter(name = "Filter object", description = "Collection of all filters for search results", example = "{}") FilterForm filters,
                               @Parameter(example = "0") Integer page,
                               @Parameter(example = "10") Integer size);

    /**
     * Get task by id
     * @param id {@link Long}, the id of the task
     * @return ResponseEntity representing the task
     */
    @Operation(description = "Get task by id", summary = "Get task by id")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('tasks.read.NO_ACCESS')")
    ResponseEntity<?> getTaskById(@Parameter(required = true, example = "1") Long id);

    /**
     * Get list of students that are currently doing this task
     * @param id {@link Long}, the id of the task
     * @return ResponseEntity representing the list of students
     */
    @Operation(description = "Get list of students that are currently doing this task", summary = "Get Task Students By Task ID")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('tasks.read.NO_ACCESS')")
    ResponseEntity<?> getTaskStudentsById(@Parameter(required = true, example = "1") Long id);

    /**
     * Get course of task by its ID
     * @param id {@link Long}, the id of the task
     * @return ResponseEntity representing the course of the task
     */
    @Operation(description = "Get course of task by its ID", summary = "Get Task Course By Task ID")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('tasks.read.NO_ACCESS')")
    ResponseEntity<?> getTaskCourseById(@Parameter(required = true, example = "1") Long id);

    /**
     * Create new task; ID field does not matter in write/edit operations
     * @param task {@link TaskSaveDTO}, the task to be created
     * @param bindingResult {@link BindingResult}, the binding result
     * @return ResponseEntity representing the created task
     */
    @Operation(description = "Create new task; ID field does not matter in write/edit operations", summary = "Create new task", tags = {"Task"})
    @ApiResponse(responseCode = "201", description = "Successful Creation")
    @PreAuthorize("!hasAuthority('tasks.write.NO_ACCESS')")
    ResponseEntity<?> createNewTask(@RequestBody TaskSaveDTO task,
                                    BindingResult bindingResult);

    /**
     * Get Task Edit DTO by its ID
     * @param id {@link Long}, the id of the task
     * @return ResponseEntity representing the Task Edit DTO
     */
    @Operation(description = "Get Task Edit DTO by its ID", summary = "Get Task Edit DTO")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('tasks.read.NO_ACCESS')")
    ResponseEntity<?> getTaskByIdForEdit(@Parameter(required = true, example = "1") Long id);

    /**
     * Edit task; ID field does not matter in write/edit operations
     * @param id {@link Long}, the id of the task to be edited
     * @param task {@link TaskSaveDTO}, the task data to be updated
     * @param bindingResult {@link BindingResult}, the binding result
     * @return ResponseEntity representing the updated task
     */
    @Operation(description = "Edit task; ID field does not matter in write/edit operations", summary = "Edit task", tags = {"Task"})
    @ApiResponse(responseCode = "200", description = "Successful Update")
    @PreAuthorize("!hasAuthority('tasks.edit.NO_ACCESS')")
    ResponseEntity<?> editTask(@Parameter(required = true, example = "1") Long id,
                               @RequestBody TaskSaveDTO task,
                               BindingResult bindingResult);

    /**
     * Delete task
     * @param id {@link Long}, the id of the task to be deleted
     * @return ResponseEntity representing the result of the deletion
     */
    @Operation(description = "Delete task", summary = "Delete task")
    @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content)
    @PreAuthorize("!hasAuthority('tasks.delete.NO_ACCESS')")
    ResponseEntity<?> deleteTask(@Parameter(required = true, example = "1") Long id);

    /**
     * Export Task To PDF
     * @param id {@link Long}, the id of the task to be exported
     * @param locale {@link String}, the locale for PDF generation
     * @return ResponseEntity representing the exported task in PDF format
     * @throws IOException if an I/O exception occurs
     */
    @Operation(summary = "Export Task To PDF", description = "Get Task in PDF format for download/open in browser purpose")
    @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content)
    @PreAuthorize("!hasAuthority('tasks.read.NO_ACCESS')")
    ResponseEntity<?> exportTaskToPDF(@Parameter(required = true, example = "1") Long id,
                                      @RequestParam(required = false, defaultValue = "ua") String locale) throws IOException;

    /**
     * Get Task Subtasks
     * @param id {@link Long}, the id of the task
     * @return ResponseEntity representing the subtasks of the task
     */
    @Operation(summary = "Get Task Subtasks", description = "Get subtasks of tasks by its ID")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('tasks.read.NO_ACCESS')")
    ResponseEntity<?> getSubtasks(@Parameter(required = true, example = "1") Long id);

    /**
     * Add Subtask To List
     * @param taskId {@link Long}, the id of the task to which the subtask will be added
     * @param dto {@link TaskSubtaskListDTO}, the DTO containing subtask information
     * @return ResponseEntity representing the result of the operation
     */
    @Operation(summary = "Add Subtask To List", description = "Add subtask to task list")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('tasks.edit.NO_ACCESS')")
    ResponseEntity<?> addSubtaskToList(@Parameter(required = true, example = "1") Long taskId,
                                       @RequestBody TaskSubtaskListDTO dto);

    /**
     * Shuffle Subtasks
     * @param taskId {@link Long}, the id of the task whose subtasks will be shuffled
     * @param request {@link SubtaskShuffleRequest}, the shuffle request
     * @return ResponseEntity representing the result of the shuffle operation
     */
    @Operation(summary = "Shuffle Subtasks", description = "Change subtasks order in task list")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('tasks.edit.NO_ACCESS')")
    ResponseEntity<?> shuffleSubtasks(@Parameter(required = true, example = "1") Long taskId,
                                      @RequestBody SubtaskShuffleRequest request);

    /**
     * Remove Subtask from Task
     * @param taskId {@link Long}, the id of the task from which the subtask will be removed
     * @param subtaskId {@link Long}, the id of the subtask to be removed
     * @return ResponseEntity representing the result of the removal operation
     */
    @Operation(summary = "Remove Subtask from Task", description = "Remove subtask from task list - this is not task deletion from database")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('tasks.edit.NO_ACCESS')")
    ResponseEntity<?> removeSubtaskFromList(@Parameter(required = true, example = "1") Long taskId,
                                            @Parameter(required = true, example = "1") Long subtaskId);

    /**
     * Get Tasks Without Course
     * @param filters {@link FilterForm}, the filters for search results
     * @param page {@link Integer}, the page number
     * @param size {@link Integer}, the size of the page
     * @return ResponseEntity representing the tasks without course
     */
    @Operation(summary = "Get Tasks Without Course", description = "Get [parent] tasks not assigned to any course")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('tasks.read.NO_ACCESS')")
    ResponseEntity<?> getTasksWithoutCourse(FilterForm filters,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "5") int size);

    /**
     * Get Available Tasks
     * @param filters {@link FilterForm}, the filters for search results
     * @param page {@link Integer}, the page number
     * @param size {@link Integer}, the size of the page
     * @return ResponseEntity representing the available tasks
     */
    @Operation(summary = "Get Available Tasks", description = "Get all [parent] tasks which may or may not be assigned to any course")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('tasks.read.NO_ACCESS')")
    ResponseEntity<?> getAvailableTasks(FilterForm filters,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "5") int size);

    /**
     * Get Task Level List
     * @return ResponseEntity representing the task levels list
     */
    @Operation(summary = "Get Task Level List", description = "Get task levels list in select format: id-name")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    ResponseEntity<?> getLevelList();

    /**
     * Get Task Status List
     * @return ResponseEntity representing the task status list
     */
    @Operation(summary = "Get Task Status List", description = "Get task status list in select format: id-name")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    ResponseEntity<?> getStatusList();
}
