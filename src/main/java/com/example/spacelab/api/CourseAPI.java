package com.example.spacelab.api;

import com.example.spacelab.dto.SelectDTO;
import com.example.spacelab.dto.course.*;
import com.example.spacelab.dto.task.TaskCourseDTO;
import com.example.spacelab.util.FilterForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Tag(name = "Course", description = "Course API")
public interface CourseAPI {

    @Operation(description = "Get list of courses paginated by 'page/size' params (default values are 0/10), output depends on permission type(full/partial)", summary = "Get courses list")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('courses.read.NO_ACCESS')")
    ResponseEntity<?> getCourses(@Parameter(name = "Filter object", description = "Collection of all filters for search results") FilterForm filters,
                                 @Parameter(example = "0") Integer page,
                                 @Parameter(example = "10") Integer size);

    @Operation(description = "Get course by id", summary = "Get course by id")
    @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CourseInfoDTO.class)))
    @PreAuthorize("!hasAuthority('courses.read.NO_ACCESS')")
    ResponseEntity<?> getCourse(@Parameter(example = "1", required = true) Long id);

    @Operation(
            description = "Get tasks for course with specified ID",
            summary = "Get Course Tasks"
    )
    @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TaskCourseDTO.class)))
    @PreAuthorize("!hasAuthority('courses.read.NO_ACCESS')")
    ResponseEntity<?> getCourseTasks(@Parameter(example = "1", required = true) Long id);

    @Operation(description = "Get course by id for edit", summary = "Get Course Edit DTO")
    @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CourseEditDTO.class)))
    @PreAuthorize("!hasAuthority('courses.read.NO_ACCESS')")
    ResponseEntity<?> getCourseForUpdate(@Parameter(example = "1", required = true) Long id);

    @Operation(description = "Get course by id for info display", summary = "Get Course Info DTO")
    @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CourseListDTO.class)))
    @PreAuthorize("!hasAuthority('courses.read.NO_ACCESS')")
    ResponseEntity<?> getCourseForInfoDisplay(@Parameter(example = "1", required = true) Long id);

    @Operation(
            description = "Create new course; ID field does not matter in write/edit operations",
            summary = "Create New Course"
    )
    @ApiResponse(responseCode = "201", description = "Created", content = @Content)
    @PreAuthorize("!hasAuthority('courses.write.NO_ACCESS')")
    ResponseEntity<?> createNewCourse(@RequestBody CourseEditDTO dto,
                                      BindingResult bindingResult);

    @Operation(
            description = "Edit course; ID field does not matter in write/edit operations",
            summary = "Edit Course"
    )
    @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content)
    @PreAuthorize("!hasAuthority('courses.edit.NO_ACCESS')")
    ResponseEntity<?> updateCourse(@Parameter(example = "1", required = true) Long id,
                                   @RequestBody CourseEditDTO dto,
                                   BindingResult bindingResult);

    @Operation(description = "Delete course", summary = "Delete Course")
    @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content)
    @PreAuthorize("!hasAuthority('courses.delete.NO_ACCESS')")
    ResponseEntity<?> deleteCourse(@Parameter(example = "1", required = true) Long id);

    @Operation(description = "Endpoint for uploading course icon", summary = "Upload Icon")
    @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content)
    @PreAuthorize("!hasAuthority('courses.edit.NO_ACCESS')")
    ResponseEntity<?> uploadIcon(@Parameter(example = "1", required = true) Long id,
                                 @ModelAttribute CourseIconDTO dto,
                                 BindingResult bindingResult) throws IOException;

    @Operation(description = "Endpoint for deleting course icon", summary = "Delete Icon")
    @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content)
    @PreAuthorize("!hasAuthority('courses.edit.NO_ACCESS')")
    ResponseEntity<?> deleteIcon(@Parameter(example = "1", required = true) Long id) throws IOException;

    @Operation(
            description = "Get courses list in select format: id-name",
            summary = "Get Courses List (Select format)"
    )
    @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(minItems = 2, schema = @Schema(implementation = CourseSelectDTO.class))))
    @PreAuthorize("!hasAuthority('courses.read.NO_ACCESS')")
    ResponseEntity<?> getCoursesIdAndNames();

    @Operation(
            description = "Get courses status list in select format: id-name, id and name are the same for statuses",
            summary = "Get Courses Status List"
    )
    @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(minItems = 2, schema = @Schema(implementation = SelectDTO.class))))
    @PreAuthorize("!hasAuthority('courses.read.NO_ACCESS')")
    ResponseEntity<?> getStatusList();
}
