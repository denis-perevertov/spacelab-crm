package com.example.spacelab.controller;

import com.example.spacelab.dto.SelectDTO;
import com.example.spacelab.dto.course.*;
import com.example.spacelab.dto.task.TaskCourseDTO;
import com.example.spacelab.exception.ErrorMessage;
import com.example.spacelab.exception.ObjectValidationException;
import com.example.spacelab.mapper.CourseMapper;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.course.CourseStatus;
import com.example.spacelab.model.role.PermissionType;
import com.example.spacelab.service.CourseService;
import com.example.spacelab.util.AuthUtil;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.validator.CourseValidator;
import com.example.spacelab.validator.CourseUpdateValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Tag(name = "Course", description = "Course controller")
@Slf4j
@Data
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final CourseMapper mapper;
    private final CourseValidator courseValidator;
    private final CourseUpdateValidator courseUpdateValidator;

    private final AuthUtil authUtil;

    // Получение списка курсов  (с фильтрами/страницами)
    @Operation(description = "Get list of courses paginated by 'page/size' params (default values are 0/10), output depends on permission type(full/partial)", summary = "Get courses list", tags = {"Course"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('courses.read.NO_ACCESS')")
    @GetMapping
    @Transactional
    public ResponseEntity<Page<CourseListDTO>> getCourses(@Parameter(name = "Filter object", description = "Collection of all filters for search results", example = "{}") FilterForm filters,
                                                          @RequestParam(required = false, defaultValue = "0") Integer page,
                                                          @RequestParam(required = false, defaultValue = "10") Integer size) {

        Page<CourseListDTO> courseListDTO = new PageImpl<>(new ArrayList<>());
        Page<Course> coursePage;
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");

        Admin loggedInAdmin = authUtil.getLoggedInAdmin();
        PermissionType permissionForLoggedInAdmin = loggedInAdmin.getRole().getPermissions().getReadCourses();
        Set<Course> adminCourses = loggedInAdmin.getCourses();

        if(permissionForLoggedInAdmin == PermissionType.FULL) {
            coursePage = courseService.getCourses(filters.trim(), pageable);
            courseListDTO = new PageImpl<>(coursePage.getContent().stream().map(mapper::fromCourseToListDTO).toList(), pageable, coursePage.getTotalElements());
        }
        else if(permissionForLoggedInAdmin == PermissionType.PARTIAL) {
            Long[] allowedCoursesIDs = adminCourses.stream().map(Course::getId).toList().toArray(Long[]::new);
            coursePage = courseService.getAllowedCourses(filters.trim(), pageable, allowedCoursesIDs);
            courseListDTO = new PageImpl<>(coursePage.getContent().stream().map(mapper::fromCourseToListDTO).toList(), pageable, coursePage.getTotalElements());
        }

        return new ResponseEntity<>(courseListDTO, HttpStatus.OK);
    }



    // Получение курса по id
    @Operation(description = "Get course by id", summary = "Get course by id", tags = {"Course"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Course not found in DB", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('courses.read.NO_ACCESS')")
    @GetMapping("/{id}")
    public ResponseEntity<CourseInfoDTO> getCourse(@PathVariable @Parameter(example = "1") Long id) {

        authUtil.checkAccessToCourse(id, "courses.read");

        CourseInfoDTO course = mapper.fromCourseToInfoDTO(courseService.getCourseById(id));
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<?> getCourseTasks(@PathVariable Long id) {
        authUtil.checkAccessToCourse(id, "courses.read");
        List<TaskCourseDTO> courseTasks =
                courseService.getCourseTasks(id)
                        .stream()
                        .map(mapper::fromTaskToCourseDTO)
                        .toList();
        return ResponseEntity.ok(courseTasks);
    }


    // Получение курса для редактирования по id
    @Operation(description = "Get course by id for edit", summary = "Get course by id for edit", tags = {"Course"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Course not found in DB", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('courses.read.NO_ACCESS')")
    @GetMapping("/update/{id}")
    public ResponseEntity<CourseEditDTO> getCourseForUpdate(@PathVariable @Parameter(example = "1") Long id) {

        authUtil.checkAccessToCourse(id, "courses.read");

        CourseEditDTO course = mapper.fromCourseToEditDTO(courseService.getCourseById(id));
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @PreAuthorize("!hasAuthority('courses.read.NO_ACCESS')")
    @GetMapping("/info/{id}")
    public ResponseEntity<CourseInfoPageDTO> getCourseForInfoDisplay(@PathVariable @Parameter(example = "1") Long id) {

        authUtil.checkAccessToCourse(id, "courses.read");

        CourseInfoPageDTO course = mapper.fromCourseToInfoPageDTO(courseService.getCourseById(id));
        return new ResponseEntity<>(course, HttpStatus.OK);
    }


    // Сохранение нового курса
    @Operation(description = "Create new course; ID field does not matter in write/edit operations",
            summary = "Create new course", tags = {"Course"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course created"),
            @ApiResponse(responseCode = "400", description = "Bad Request / Validation Error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('courses.write.NO_ACCESS')")
    @PostMapping
    public ResponseEntity<?> createNewCourse( @RequestBody CourseEditDTO dto,
                                                   BindingResult bindingResult) {

        dto.setId(null);
        log.info("DTO to save : " + dto);
        authUtil.checkPermissionToCreateCourse();

        courseValidator.validate(dto, bindingResult);

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ObjectValidationException(errors);
        }

        Course course = courseService.createNewCourse(dto);
        return new ResponseEntity<>(course.getId(), HttpStatus.CREATED);
    }



    // Сохранение изменениий курса
    @Operation(description = "Edit course; ID field does not matter in write/edit operations", summary = "Edit course", tags = {"Course"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course updated"),
            @ApiResponse(responseCode = "400", description = "Bad Request / Validation Error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Course not found in DB", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('courses.edit.NO_ACCESS')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable @Parameter(example = "1") Long id,
                                           @RequestBody CourseEditDTO dto,
                                               BindingResult bindingResult) {

        dto.setId(id);
        log.info("DTO to save : " + dto);
        authUtil.checkAccessToCourse(dto.getId(), "courses.edit");
        authUtil.checkAccessToCourse(id, "courses.edit");

        courseValidator.validate(dto, bindingResult);

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ObjectValidationException(errors);
        }

        courseService.removeAdminsFromCourse(dto.getId());
        Course course = courseService.editCourse(dto);
        return new ResponseEntity<>(course.getId(), HttpStatus.OK);
    }

    // Удаление по id
    @Operation(description = "Delete course", summary = "Delete course", tags = {"Course"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Course not found in DB", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('courses.delete.NO_ACCESS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable @Parameter(example = "1") Long id) {

        authUtil.checkAccessToCourse(id, "courses.delete");

        courseService.deleteCourseById(id);
        return new ResponseEntity<>("Course deleted", HttpStatus.OK);
    }

    // Загрузка иконки
    @PostMapping("/{id}/icon")
    @ResponseBody
    public ResponseEntity<?> uploadIcon(@PathVariable Long id,
                                        @ModelAttribute CourseIconDTO dto,
                                        BindingResult bindingResult) throws IOException {
        courseValidator.validateIcon(dto, bindingResult);
        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        courseService.saveIcon(id, dto);
        return ResponseEntity.ok("Created");
    }

    // Очистка иконки
    @DeleteMapping("/{id}/icon")
    public ResponseEntity<?> deleteIcon(@PathVariable Long id) throws IOException {
        courseService.deleteIcon(id);
        return ResponseEntity.ok("Deleted");
    }

    // Select2
    @Operation(description = "Get courses for select2", summary = "Get courses for select2", tags = {"Course"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
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

    @GetMapping("/get-all-courses")
    @ResponseBody
    public List<CourseSelectDTO> getCoursesIdAndNames() {
        return courseService.getCourses().stream().map(mapper::fromCourseToSelectDTO).toList();
    }

    @GetMapping("/get-status-list")
    public ResponseEntity<?> getStatusList() {
        return ResponseEntity.ok(Arrays.stream(CourseStatus.values()).map(v -> new SelectDTO(v.name(), v.name())).toList());
    }
}
