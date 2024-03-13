package com.example.spacelab.api;

import com.example.spacelab.util.FilterForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Student Task", description = "Student Task API")
public interface StudentTaskAPI {

    /**
     * Retrieve all tasks of a single student with pagination and filtering options.
     * @param filters {@link FilterForm}, filter object
     * @param page {@link int}, default value 0
     * @param size {@link int}, default value 10
     * @return {@link ResponseEntity} representing the object
     */
    @Operation(summary = "Get Tasks of Single Student", description = "Retrieve all tasks of a single student with pagination and filtering options")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    ResponseEntity<Object> getTasksOfSingleStudent(FilterForm filters, int page, int size);

    /**
     * Retrieve a single task of a single student by task ID.
     * @param taskID {@link Long} representing the task ID
     * @return {@link ResponseEntity} representing the object
     */
    @Operation(summary = "Get Single Task of Single Student", description = "Retrieve a single task of a single student by task ID")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    ResponseEntity<Object> getSingleTaskOfSingleStudent(Long taskID);

    /**
     * Retrieve information about a specific student task.
     * @param taskID {@link Long} representing the task ID
     * @return {@link ResponseEntity} representing the object
     */
    @Operation(summary = "Get Student Task Info", description = "Retrieve information about a specific student task")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    ResponseEntity<?> getStudentTaskInfo(Long taskID);

    /**
     * Get progress points of a student task.
     * @param taskID {@link Long} representing the task ID
     * @return {@link ResponseEntity} representing the object
     */
    @Operation(summary = "Get Task Progress Points", description = "Get progress points of a student task")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    ResponseEntity<?> getTaskProgressPoints(Long taskID);

    /**
     * Complete a student task.
     * @param taskID {@link Long} representing the task ID
     * @return {@link ResponseEntity} representing the object
     */
    @Operation(summary = "Complete Student Task", description = "Complete a student task")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    ResponseEntity<Object> completeStudentTask(Long taskID);

    /**
     * Unlock a student task.
     * @param taskID {@link Long} representing the task ID
     * @return {@link ResponseEntity} representing the object
     */
    @Operation(summary = "Unlock Student Task", description = "Unlock a student task")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    ResponseEntity<?> unlockStudentTask(Long taskID);

    /**
     * Lock a student task.
     * @param taskID {@link Long} representing the task ID
     * @return {@link ResponseEntity} representing the object
     */
    @Operation(summary = "Lock Student Task", description = "Lock a student task")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    ResponseEntity<?> lockStudentTask(Long taskID);

    /**
     * Reset a student task.
     * @param taskID {@link Long} representing the task ID
     * @return {@link ResponseEntity} representing the object
     */
    @Operation(summary = "Reset Student Task", description = "Reset a student task")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    ResponseEntity<?> resetStudentTask(Long taskID);

}
