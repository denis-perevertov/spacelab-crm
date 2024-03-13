package com.example.spacelab.controller;

import com.example.spacelab.api.LessonAPI;
import com.example.spacelab.dto.SelectDTO;
import com.example.spacelab.dto.lesson.LessonInfoDTO;
import com.example.spacelab.dto.lesson.LessonListDTO;
import com.example.spacelab.dto.lesson.LessonReportRowSaveDTO;
import com.example.spacelab.dto.lesson.LessonSaveBeforeStartDTO;
import com.example.spacelab.exception.ErrorMessage;
import com.example.spacelab.exception.ObjectValidationException;
import com.example.spacelab.mapper.LessonMapper;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.lesson.Lesson;
import com.example.spacelab.model.lesson.LessonReportRow;
import com.example.spacelab.model.lesson.LessonStatus;
import com.example.spacelab.model.role.PermissionType;
import com.example.spacelab.service.LessonReportRowService;
import com.example.spacelab.service.LessonService;
import com.example.spacelab.util.AuthUtil;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.validator.LessonBeforeStartValidator;
import com.example.spacelab.validator.LessonReportRowValidator;
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
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lessons")
public class LessonController implements LessonAPI {

    private final LessonReportRowService lessonReportRowService;
    private final LessonService lessonService;
    private final LessonMapper mapper;
    private final LessonBeforeStartValidator lessonBeforeStartValidator;
    private final LessonReportRowValidator lessonReportRowValidator;

    private final AuthUtil authUtil;


    @GetMapping
    public ResponseEntity<Page<LessonListDTO>> getLesson(@Parameter(name = "Filter object", description = "Collection of all filters for search results", example = "{}") FilterForm filters,
                                                                 @RequestParam(required = false, defaultValue = "0") Integer page,
                                                                 @RequestParam(required = false, defaultValue = "10") Integer size) {

        Page<LessonListDTO> lessons;
        Page<Lesson> lessonPage = new PageImpl<>(new ArrayList<>());
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");

        Admin loggedInAdmin = authUtil.getLoggedInAdmin();
        PermissionType permissionForLoggedInAdmin = loggedInAdmin.getRole().getPermissions().getReadStudents();
        Set<Course> adminCourses = loggedInAdmin.getCourses();

        if(permissionForLoggedInAdmin == PermissionType.FULL) {
            lessonPage = lessonService.getLessons(filters.trim(), pageable);
        }
        else if(permissionForLoggedInAdmin == PermissionType.PARTIAL) {
            Long[] allowedCoursesIDs = adminCourses.stream().map(Course::getId).toList().toArray(Long[]::new);
            lessonPage = lessonService.getLessonsByAllowedCourses(filters.trim(), pageable, allowedCoursesIDs);
        }
        lessons = new PageImpl<>(lessonPage.getContent().stream().map(mapper::fromLessonToLessonListDTO).toList(), pageable, lessonPage.getTotalElements());

        return new ResponseEntity<>(lessons, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonInfoDTO> getLessonById(@PathVariable @Parameter(example = "1") Long id) {

        authUtil.checkAccessToCourse(lessonService.getLessonById(id).getCourse().getId(), "lessons.read");

        LessonInfoDTO less = mapper.fromLessonToLessonInfoDTO(lessonService.getLessonById(id));
        return new ResponseEntity<>(less, HttpStatus.OK);
    }

    @GetMapping("/{id}/update")
    public ResponseEntity<?> getLessonInfoForUpdate(@PathVariable Long id) {
        authUtil.checkAccessToCourse(lessonService.getLessonById(id).getCourse().getId(), "lessons.edit");
        return ResponseEntity.ok(mapper.fromLessonToEditDTO(lessonService.getLessonById(id)));
    }

    @PostMapping
    public ResponseEntity<?> createNewLessonBeforeStart(@RequestBody LessonSaveBeforeStartDTO lesson,
                                                             BindingResult bindingResult) {

        log.info("lesson data: " + lesson);

        authUtil.checkAccessToCourse(lesson.getCourseID(), "lessons.write");

        lesson.setId(null);
        lesson.setStatus(LessonStatus.PLANNED);

        lessonBeforeStartValidator.validate(lesson, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ObjectValidationException(errors);
        }
        Lesson l = lessonService.createNewLesson(mapper.BeforeStartDTOtoLesson(lesson));
        return new ResponseEntity<>(l.getId(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editLessonBeforeStart(@PathVariable @Parameter(example = "1") Long id,
                                                        @RequestBody LessonSaveBeforeStartDTO lesson, BindingResult bindingResult) {

        authUtil.checkAccessToCourse(lessonService.getLessonById(id).getCourse().getId(), "lessons.edit");
        authUtil.checkAccessToCourse(lesson.getCourseID(), "lessons.edit");

        lesson.setId(id);

        lessonBeforeStartValidator.validate(lesson, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ObjectValidationException(errors);
        }
        Lesson l = lessonService.editLesson(mapper.BeforeStartDTOtoLesson(lesson));
        return new ResponseEntity<>(l.getId(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLesson(@PathVariable @Parameter(example = "1") Long id) {

        authUtil.checkAccessToCourse(lessonService.getLessonById(id).getCourse().getId(), "lessons.delete");

        lessonService.deleteLessonById(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

    @GetMapping("/{id}/get-report-rows")
    public ResponseEntity<?> getLessonReportRows(@PathVariable Long id) {
        authUtil.checkAccessToCourse(lessonService.getLessonById(id).getCourse().getId(), "lessons.read");
        return ResponseEntity.ok(mapper.fromReportRowListToDTOList(lessonService.getLessonById(id).getReportRows()));
    }

    @PostMapping("/save-report-row")
    public ResponseEntity<?> saveLessonReportRowAfterStart(@RequestBody @Valid LessonReportRowSaveDTO dto,
                                                           BindingResult bindingResult) {
        authUtil.checkAccessToCourse(lessonService.getLessonById(dto.getLessonId()).getCourse().getId(), "lessons.write");
        lessonReportRowValidator.validate(dto, bindingResult);
        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ObjectValidationException(errors);
        }
        LessonReportRow reportRow = lessonReportRowService.updateLessonReportRow(dto);
        return new ResponseEntity<>(mapper.fromReportRowToDTO(reportRow), HttpStatus.OK);
    }

    // ========================

    @GetMapping("/{id}/start")
    public ResponseEntity<String> startLesson(@PathVariable @Parameter(example = "1") Long id) {
        authUtil.checkAccessToCourse(lessonService.getLessonById(id).getCourse().getId(), "lessons.write");
        lessonService.startLesson(id);
        return new ResponseEntity<>("Lesson started", HttpStatus.ACCEPTED);
    }

    @GetMapping("/{id}/complete")
    public ResponseEntity<String> completeLesson(@PathVariable @Parameter(example = "1") Long id) {
        authUtil.checkAccessToCourse(lessonService.getLessonById(id).getCourse().getId(), "lessons.write");
        lessonService.completeLesson(id);
        return new ResponseEntity<>("Lesson completed", HttpStatus.ACCEPTED);
    }

    @GetMapping("/{id}/student-lesson-display-data")
    public ResponseEntity<?> getStudentLessonDisplayData(@PathVariable Long id) {
        authUtil.checkAccessToCourse(lessonService.getLessonById(id).getCourse().getId(), "lessons.read");
        return ResponseEntity.ok(lessonService.getStudentLessonDisplayData(id));
    }

    @GetMapping("/get-status-list")
    public ResponseEntity<?> getStatusList() {
        return ResponseEntity.ok(Arrays.stream(LessonStatus.values()).map(v -> new SelectDTO(v.name(), v.name())).toList());
    }


}
