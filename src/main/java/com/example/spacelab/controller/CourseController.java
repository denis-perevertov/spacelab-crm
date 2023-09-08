package com.example.spacelab.controller;

import com.example.spacelab.dto.course.*;
import com.example.spacelab.exception.ErrorMessage;
import com.example.spacelab.exception.ObjectValidationException;
import com.example.spacelab.mapper.CourseMapper;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.role.PermissionType;
import com.example.spacelab.service.CourseService;
import com.example.spacelab.util.AuthUtil;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.validator.CourseCreateValidator;
import com.example.spacelab.validator.CourseUpdateValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "Course", description = "Course controller")
@RestController
@Log
@Data
@RequiredArgsConstructor
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final CourseMapper mapper;
    private final CourseCreateValidator courseCreateValidator;
    private final CourseUpdateValidator courseUpdateValidator;

    // Получение списка курсов  (с фильтрами/страницами)
    @Operation(description = "Get courses list", summary = "Get courses list", tags = {"Course"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('courses.read.NO_ACCESS')")
    @GetMapping
    public ResponseEntity<Page<CourseListDTO>> getCourses(@AuthenticationPrincipal Admin loggedInAdmin,
                                                           FilterForm filters,
                                                           @RequestParam(required = false) Integer page,
                                                           @RequestParam(required = false) Integer size) {

        log.info(mapper.toString());
        log.info(courseCreateValidator.toString());
        log.info(courseUpdateValidator.toString());
        log.info(courseService.toString());

        Page<CourseListDTO> courseListDTO = new PageImpl<>(new ArrayList<>());

//        Admin loggedInAdmin = AuthUtil.getLoggedInAdmin();
        PermissionType permissionForLoggedInAdmin = loggedInAdmin.getRole().getPermissions().getReadCourses();

        if(permissionForLoggedInAdmin == PermissionType.FULL) {
            if(page == null && size == null) courseListDTO = new PageImpl<>(courseService.getCourses().stream().map(mapper::fromCourseToListDTO).toList());
            else if(page != null && size == null) courseListDTO = new PageImpl<>(courseService.getCourses(PageRequest.of(page, 10)).stream().map(mapper::fromCourseToListDTO).toList());
            else courseListDTO = new PageImpl<>(courseService.getCourses(filters, PageRequest.of(page, size)).stream().map(mapper::fromCourseToListDTO).toList());
        }
        else if(permissionForLoggedInAdmin == PermissionType.PARTIAL) {

            Long[] allowedCoursesIDs = (Long[]) loggedInAdmin.getCourses().stream().map(Course::getId).toArray();

            if(page == null && size == null) courseListDTO = new PageImpl<>(courseService.getAllowedCourses(allowedCoursesIDs).stream().map(mapper::fromCourseToListDTO).toList());
            else if(page != null && size == null) courseListDTO = new PageImpl<>(courseService.getAllowedCourses(PageRequest.of(page, 10), allowedCoursesIDs).stream().map(mapper::fromCourseToListDTO).toList());
            else courseListDTO = new PageImpl<>(courseService.getAllowedCourses(filters, PageRequest.of(page, size), allowedCoursesIDs).stream().map(mapper::fromCourseToListDTO).toList());

        }

        return new ResponseEntity<>(courseListDTO, HttpStatus.OK);
    }



    // Получение курса по id
    @Operation(description = "Get course by id", summary = "Get course by id", tags = {"Course"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Course not found in DB", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('courses.read.NO_ACCESS')")
    @GetMapping("/{id}")
    public ResponseEntity<CourseInfoDTO> getCourse(@PathVariable Long id) {

        AuthUtil.checkAccessToCourse(id, "courses.read");

        CourseInfoDTO course = mapper.fromCourseToInfoDTO(courseService.getCourseById(id));
        return new ResponseEntity<>(course, HttpStatus.OK);
    }



    // Получение курса для редактирования по id
    @Operation(description = "Get course by id for edit", summary = "Get course by id for edit", tags = {"Course"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Course not found in DB", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('courses.read.NO_ACCESS')")
    @GetMapping("/update/{id}")
    public ResponseEntity<CourseCardDTO> getCourseForUpdate(@PathVariable Long id) {

        AuthUtil.checkAccessToCourse(id, "courses.read");

        CourseCardDTO course = mapper.fromCardDTOtoCourse(courseService.getCourseById(id));
        return new ResponseEntity<>(course, HttpStatus.OK);
    }



    // Сохранение нового курса
    @Operation(description = "Create new course", summary = "Create new course", tags = {"Course"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('courses.write.NO_ACCESS')")
    @PostMapping
    public ResponseEntity<String> createNewCourse( @RequestBody CourseSaveCreatedDTO dto, BindingResult bindingResult) {

        dto.setId(null);

        AuthUtil.checkPermissionToCreateCourse();

        courseCreateValidator.validate(dto, bindingResult);

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ObjectValidationException(errors);
        }

        courseService.createNewCourse(mapper.fromSaveCreatedDTOtoCourse(dto));
        return new ResponseEntity<>("Course created", HttpStatus.CREATED);
    }



    // Сохранение изменениий курса
    @Operation(description = "Edit course", summary = "Edit course", tags = {"Course"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "404", description = "Course not found in DB", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('courses.edit.NO_ACCESS')")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCourse(@PathVariable Long id,  @RequestBody CourseSaveUpdatedDTO dto, BindingResult bindingResult) {

        dto.setId(id);

        AuthUtil.checkAccessToCourse(dto.getId(), "courses.edit");
        AuthUtil.checkAccessToCourse(id, "courses.edit");

        courseUpdateValidator.validate(dto, bindingResult);

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ObjectValidationException(errors);
        }

        courseService.createNewCourse(mapper.fromSaveUpdatedDTOtoCourse(dto));
        return new ResponseEntity<>("Course updated", HttpStatus.OK);
    }



    // Удаление по id
    @Operation(description = "Delete course", summary = "Delete course", tags = {"Course"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course deleted"),
            @ApiResponse(responseCode = "404", description = "Course not found in DB", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('courses.delete.NO_ACCESS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) {

        AuthUtil.checkAccessToCourse(id, "courses.delete");

        courseService.deleteCourseById(id);
        return new ResponseEntity<>("Course deleted", HttpStatus.OK);
    }



    // Select2
    @Operation(description = "Get courses for select2", summary = "Get courses for select2", tags = {"Course"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('courses.read.NO_ACCESS')")
    @GetMapping("/getCourses")
    @ResponseBody
    public Page<CourseSelectDTO> getOwners(@RequestParam(name = "searchQuery", defaultValue = "") String searchQuery,
                                           @RequestParam(name = "page", defaultValue = "0") int page,
                                           @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Course> courses = courseService.getCoursesByName(searchQuery, pageable);
        Page<CourseSelectDTO> ownerPage = courses.map(mapper::fromCourseToSelectDTO);
        return ownerPage;
    }
}
