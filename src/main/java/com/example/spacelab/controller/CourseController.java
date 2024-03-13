package com.example.spacelab.controller;

import com.example.spacelab.api.CourseAPI;
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
import com.example.spacelab.validator.CourseUpdateValidator;
import com.example.spacelab.validator.CourseValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
public class CourseController implements CourseAPI {

    private final CourseService courseService;
    private final CourseMapper mapper;
    private final CourseValidator courseValidator;

    private final AuthUtil authUtil;

    @GetMapping
    @Transactional
    public ResponseEntity<?> getCourses(FilterForm filters,
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourse(@PathVariable Long id) {

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

    @GetMapping("/update/{id}")
    public ResponseEntity<?> getCourseForUpdate(@PathVariable Long id) {

        authUtil.checkAccessToCourse(id, "courses.read");

        CourseEditDTO course = mapper.fromCourseToEditDTO(courseService.getCourseById(id));
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<?> getCourseForInfoDisplay(@PathVariable Long id) {

        authUtil.checkAccessToCourse(id, "courses.read");

        CourseInfoPageDTO course = mapper.fromCourseToInfoPageDTO(courseService.getCourseById(id));
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id,
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {

        authUtil.checkAccessToCourse(id, "courses.delete");

        courseService.deleteCourseById(id);
        return new ResponseEntity<>("Course deleted", HttpStatus.OK);
    }

    @PostMapping("/{id}/icon")
    public ResponseEntity<?> uploadIcon(@PathVariable Long id,
                                        @ModelAttribute CourseIconDTO dto,
                                        BindingResult bindingResult) throws IOException {

        authUtil.checkAccessToCourse(id, "courses.write");

        courseValidator.validateIcon(dto, bindingResult);
        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        courseService.saveIcon(id, dto);
        return ResponseEntity.ok("Created");
    }

    @DeleteMapping("/{id}/icon")
    public ResponseEntity<?> deleteIcon(@PathVariable Long id) throws IOException {
        courseService.deleteIcon(id);
        return ResponseEntity.ok("Deleted");
    }

    @GetMapping("/get-all-courses")
    public ResponseEntity<?> getCoursesIdAndNames() {
        Admin loggedInAdmin = authUtil.getLoggedInAdmin();
        List<Course> courses =
                loggedInAdmin.getRole().getPermissions().getReadCourses().equals(PermissionType.FULL)
                ? courseService.getCourses()
                : courseService.getAllowedCourses(authUtil.getLoggedInAdmin().getCourses().stream().map(Course::getId).toArray(Long[]::new));
        return  ResponseEntity.ok(courses
                .stream()
                .map(mapper::fromCourseToSelectDTO)
                .toList());
    }

    @GetMapping("/get-status-list")
    public ResponseEntity<?> getStatusList() {
        return ResponseEntity.ok(Arrays.stream(CourseStatus.values()).map(v -> new SelectDTO(v.name(), v.name())).toList());
    }
}
