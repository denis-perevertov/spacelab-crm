package com.example.spacelab.api;

import com.example.spacelab.dto.lesson.LessonReportRowSaveDTO;
import com.example.spacelab.dto.lesson.LessonSaveBeforeStartDTO;
import com.example.spacelab.util.FilterForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;

@Tag(name="Lesson", description = "Lesson API")
public interface LessonAPI {

    /**
     * Get lesson paginated by 'page/size' params (default values are 0/10), output depends on permission type(full/partial)
     * @param filters {@link FilterForm}, filter object
     * @param page {@link Integer}, default value 0
     * @param size {@link Integer}, default value 10
     * @return ResponseEntity representing the lesson
     */
    @Operation(description = "Get lesson paginated by 'page/size' params (default values are 0/10), output depends on permission type(full/partial)", summary = "Get lesson")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('lessons.read.NO_ACCESS')")
    ResponseEntity<?> getLesson(@Parameter(name = "Filter object", description = "Collection of all filters for search results", example = "{}") FilterForm filters,
                                @Parameter(example = "0") Integer page,
                                @Parameter(example = "10") Integer size);

    /**
     * Get lesson by id
     * @param id {@link Long}, the id of the lesson
     * @return ResponseEntity representing the lesson
     */
    @Operation(description = "Get lesson by id", summary = "Get lesson by id")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('lessons.read.NO_ACCESS')")
    ResponseEntity<?> getLessonById(@Parameter(required = true, example = "1") Long id);

    /**
     * Get lesson info for update
     * @param id {@link Long}, the id of the lesson
     * @return ResponseEntity representing the lesson info
     */
    @Operation(description = "Get lesson info for update", summary = "Get lesson info for update")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('lessons.read.NO_ACCESS')")
    ResponseEntity<?> getLessonInfoForUpdate(@Parameter(required = true, example = "1") Long id);

    /**
     * Create new lesson before start
     * @param lesson {@link LessonSaveBeforeStartDTO}, the lesson to be created
     * @param bindingResult {@link BindingResult}, the binding result
     * @return ResponseEntity representing the created lesson
     */
    @Operation(description = "Create new lesson before start", summary = "Create new lesson before start")
    @ApiResponse(responseCode = "201", description = "Successful Creation")
    @PreAuthorize("!hasAuthority('lessons.write.NO_ACCESS')")
    ResponseEntity<?> createNewLessonBeforeStart(@RequestBody LessonSaveBeforeStartDTO lesson,
                                                 BindingResult bindingResult);

    /**
     * Edit lesson before start
     * @param id {@link Long}, the id of the lesson to be edited
     * @param lesson {@link LessonSaveBeforeStartDTO}, the lesson data to be updated
     * @param bindingResult {@link BindingResult}, the binding result
     * @return ResponseEntity representing the updated lesson
     */
    @Operation(description = "Edit lesson before start", summary = "Edit lesson before start")
    @ApiResponse(responseCode = "200", description = "Successful Update")
    @PreAuthorize("!hasAuthority('lessons.edit.NO_ACCESS')")
    ResponseEntity<?> editLessonBeforeStart(@Parameter(required = true, example = "1") Long id,
                                            @RequestBody LessonSaveBeforeStartDTO lesson,
                                            BindingResult bindingResult);

    /**
     * Delete lesson
     * @param id {@link Long}, the id of the lesson to be deleted
     * @return ResponseEntity representing the result of the deletion
     */
    @Operation(description = "Delete lesson", summary = "Delete lesson")
    @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content)
    @PreAuthorize("!hasAuthority('lessons.delete.NO_ACCESS')")
    ResponseEntity<?> deleteLesson(@Parameter(required = true, example = "1") Long id);

    /**
     * Get lesson report rows
     * @param id {@link Long}, the id of the lesson
     * @return ResponseEntity representing the lesson report rows
     */
    @Operation(description = "Get lesson report rows", summary = "Get lesson report rows")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('lessons.read.NO_ACCESS')")
    ResponseEntity<?> getLessonReportRows(Long id);

    /**
     * Save lesson report row after start
     * @param dto {@link LessonReportRowSaveDTO}, the DTO containing lesson report row information
     * @param bindingResult {@link BindingResult}, the binding result
     * @return ResponseEntity representing the result of the operation
     */
    @Operation(description = "Save lesson report row after start", summary = "Save lesson report row after start")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('lessons.write.NO_ACCESS')")
    ResponseEntity<?> saveLessonReportRowAfterStart(@RequestBody LessonReportRowSaveDTO dto,
                                                    BindingResult bindingResult);

    /**
     * Start lesson
     * @param id {@link Long}, the id of the lesson to be started
     * @return ResponseEntity representing the result of the operation
     */
    @Operation(description = "Start lesson", summary = "Start lesson")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('lessons.write.NO_ACCESS')")
    ResponseEntity<?> startLesson(@Parameter(required = true, example = "1") Long id);

    /**
     * Complete lesson
     * @param id {@link Long}, the id of the lesson to be completed
     * @return ResponseEntity representing the result of the operation
     */
    @Operation(description = "Complete lesson", summary = "Complete lesson")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('lessons.write.NO_ACCESS')")
    ResponseEntity<?> completeLesson(@Parameter(required = true, example = "1") Long id);

    /**
     * Get student lesson display data
     * @param id {@link Long}, the id of the lesson
     * @return ResponseEntity representing the student lesson display data
     */
    @Operation(description = "Get student lesson display data", summary = "Get student lesson display data")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('lessons.read.NO_ACCESS')")
    ResponseEntity<?> getStudentLessonDisplayData(@Parameter(required = true, example = "1") Long id);

    /**
     * Get lesson status list
     * @return ResponseEntity representing the lesson status list
     */
    @Operation(summary = "Get Lesson Status List", description = "Get lesson status list in select format: id-name")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    ResponseEntity<?> getStatusList();
}
