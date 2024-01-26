package com.example.spacelab.controller;

import com.example.spacelab.dto.SelectDTO;
import com.example.spacelab.dto.lesson.LessonInfoDTO;
import com.example.spacelab.dto.lesson.LessonListDTO;
import com.example.spacelab.dto.lesson.LessonReportRowSaveDTO;
import com.example.spacelab.dto.lesson.LessonSaveBeforeStartDTO;
import com.example.spacelab.exception.ErrorMessage;
import com.example.spacelab.exception.ObjectValidationException;
import com.example.spacelab.job.LessonMonitor;
import com.example.spacelab.mapper.LessonMapper;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.lesson.Lesson;
import com.example.spacelab.model.lesson.LessonReportRow;
import com.example.spacelab.model.lesson.LessonStatus;
import com.example.spacelab.model.literature.LiteratureType;
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
import org.apache.coyote.Response;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name="Lesson", description = "Lesson controller")
@RestController
@Log
@RequiredArgsConstructor
@RequestMapping("/api/lessons")
public class LessonController {

    private final LessonReportRowService lessonReportRowService;
    private final LessonService lessonService;
    private final LessonMapper mapper;
    private final LessonBeforeStartValidator lessonBeforeStartValidator;
    private final LessonReportRowValidator lessonReportRowValidator;

    private final AuthUtil authUtil;


    //Получение списка уроков по фильтру и пагинации
    @Operation(description = "Get list of lessons paginated by 'page/size' params (default values are 0/10), output depends on permission type(full/partial)", summary = "Get lesson list", tags = {"Lesson"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('lessons.read.NO_ACCESS')")
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
            lessonPage = lessonService.getLessons(filters, pageable);
        }
        else if(permissionForLoggedInAdmin == PermissionType.PARTIAL) {
            Long[] allowedCoursesIDs = adminCourses.stream().map(Course::getId).toList().toArray(new Long[adminCourses.size()]);
            lessonPage = lessonService.getLessonsByAllowedCourses(filters, pageable, allowedCoursesIDs);
        }
        lessons = new PageImpl<>(lessonPage.getContent().stream().map(mapper::fromLessonToLessonListDTO).toList(), pageable, lessonPage.getTotalElements());

        return new ResponseEntity<>(lessons, HttpStatus.OK);
    }

    //Получение урока по id
    @Operation(description = "Get lesson by ID", summary = "Get lesson by ID", tags = {"Lesson"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Lesson not found in DB", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('lessons.read.NO_ACCESS')")
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

    //Создание урока
    @Operation(description = "Create new lesson w/ PLANNED status; ID field does not matter in write/edit operations", summary = "Create new lesson", tags = {"Lesson"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful save"),
            @ApiResponse(responseCode = "400", description = "Bad Request / Validation Error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('lessons.write.NO_ACCESS')")
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


    //Редактирование урока
    @Operation(description = "Edit lesson; ID field does not matter in write/edit operations", summary = "Edit lesson", tags = {"Lesson"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful update"),
            @ApiResponse(responseCode = "400", description = "Bad Request / Validation Error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Lesson not found in DB", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('lessons.edit.NO_ACCESS')")
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

    //Удаление урока
    @Operation(description = "Delete lesson", summary = "Delete lesson", tags = {"Lesson"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful delete"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Lesson not found in DB", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('lessons.delete.NO_ACCESS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLesson(@PathVariable @Parameter(example = "1") Long id) {

        authUtil.checkAccessToCourse(lessonService.getLessonById(id).getCourse().getId(), "lessons.delete");

        lessonService.deleteLessonById(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

    @GetMapping("/{id}/get-report-rows")
    public ResponseEntity<?> getLessonReportRows(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.fromReportRowListToDTOList(lessonService.getLessonById(id).getReportRows()));
    }

    //Сохранение ответа студента на уроке
//    @Operation(description = "Save student's report to lesson report table", summary = "Save Student Info Into Report Table", tags = {"Lesson"})
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successful start"),
//            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
//            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
//            @ApiResponse(responseCode = "404", description = "Lesson not found in DB", content = @Content),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
//    })
//    @PreAuthorize("!hasAuthority('lessons.edit.NO_ACCESS')")
    @PostMapping("/save-report-row")
    public ResponseEntity<?> saveLessonReportRowAfterStart(@RequestBody @Valid LessonReportRowSaveDTO dto,
                                                           BindingResult bindingResult) {
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

    @Operation(description = "Start a lesson (change its status to ACTIVE). Can't start an already completed/active lesson", summary = "Start Lesson", tags = {"Lesson"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/{id}/start")
    public ResponseEntity<String> startLesson(@PathVariable @Parameter(example = "1") Long id) {
        lessonService.startLesson(id);
        return new ResponseEntity<>("Lesson started", HttpStatus.ACCEPTED);
    }

    @Operation(description = "Stop a lesson (change its status to COMPLETED). Can't stop an already completed/planned lesson", summary = "Stop Lesson", tags = {"Lesson"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/{id}/complete")
    public ResponseEntity<String> completeLesson(@PathVariable @Parameter(example = "1") Long id) {
        lessonService.completeLesson(id);
        return new ResponseEntity<>("Lesson completed", HttpStatus.ACCEPTED);
    }

    // получение всех страничек данных у студентов курса занятия
    @GetMapping("/{id}/student-lesson-display-data")
    public ResponseEntity<?> getStudentLessonDisplayData(@PathVariable Long id) {
        return ResponseEntity.ok(lessonService.getStudentLessonDisplayData(id));
    }

    // Получение списка cтатусов
    @GetMapping("/get-status-list")
    public ResponseEntity<?> getStatusList() {
        return ResponseEntity.ok(Arrays.stream(LessonStatus.values()).map(v -> new SelectDTO(v.name(), v.name())).toList());
    }


}
