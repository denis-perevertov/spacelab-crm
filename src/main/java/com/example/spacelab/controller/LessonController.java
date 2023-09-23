package com.example.spacelab.controller;

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
import com.example.spacelab.model.lesson.LessonStatus;
import com.example.spacelab.model.role.PermissionType;
import com.example.spacelab.service.LessonReportRowService;
import com.example.spacelab.service.LessonService;
import com.example.spacelab.util.AuthUtil;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.validator.LessonBeforeStartValidator;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    private final LessonMonitor monitor;

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

        Page<LessonListDTO> dtoList = new PageImpl<>(new ArrayList<>());

        Admin loggedInAdmin = authUtil.getLoggedInAdmin();
        PermissionType permissionForLoggedInAdmin = loggedInAdmin.getRole().getPermissions().getReadCourses();

        if(permissionForLoggedInAdmin == PermissionType.FULL) {
            if(page == null && size == null) dtoList = new PageImpl<>(lessonService.getLesson().stream().map(mapper::fromLessonToLessonListDTO).toList());
            else if(page != null && size == null) dtoList = new PageImpl<>(lessonService.getLesson(PageRequest.of(page, 10)).stream().map(mapper::fromLessonToLessonListDTO).toList());
            else dtoList = new PageImpl<>(lessonService.getLesson(filters, PageRequest.of(page, size)).stream().map(mapper::fromLessonToLessonListDTO).toList());
        }
        else if(permissionForLoggedInAdmin == PermissionType.PARTIAL) {

            Long[] allowedCoursesIDs = (Long[]) loggedInAdmin.getCourses().stream().map(Course::getId).toArray();

            if(page == null && size == null) dtoList = new PageImpl<>(lessonService.getLessonsByAllowedCourses(allowedCoursesIDs).stream().map(mapper::fromLessonToLessonListDTO).toList());
            else if(page != null && size == null) dtoList = new PageImpl<>(lessonService.getLessonsByAllowedCourses(PageRequest.of(page, 10), allowedCoursesIDs).stream().map(mapper::fromLessonToLessonListDTO).toList());
            else dtoList = new PageImpl<>(lessonService.getLessonsByAllowedCourses(filters, PageRequest.of(page, size), allowedCoursesIDs).stream().map(mapper::fromLessonToLessonListDTO).toList());

        }

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
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
    public ResponseEntity<String> createNewLessonBeforeStart(@RequestBody LessonSaveBeforeStartDTO lesson, BindingResult bindingResult) {

        authUtil.checkAccessToCourse(lesson.getCourseID(), "lessons.write");

        lesson.setId(null);
        lesson.setStatus(LessonStatus.PLANNED);

        lessonBeforeStartValidator.validate(lesson, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ObjectValidationException(errors);
        }
        lessonService.createNewLesson(mapper.BeforeStartDTOtoLesson(lesson));
        return new ResponseEntity<>("Successful save", HttpStatus.CREATED);
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
    public ResponseEntity<String> editLessonBeforeStart(@PathVariable @Parameter(example = "1") Long id,
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
        lessonService.editLesson(mapper.BeforeStartDTOtoLesson(lesson));
        return new ResponseEntity<>("Successful update", HttpStatus.OK);
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

    //Сохранение ответа студента на уроке
    @Operation(description = "Save student's report to lesson report table", summary = "Save Student Info Into Report Table", tags = {"Lesson"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful start"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Lesson not found in DB", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('lessons.edit.NO_ACCESS')")
    @PostMapping("/update")
    public ResponseEntity<String> saveLessonReportRowAfterStart(@RequestBody LessonReportRowSaveDTO lessonReportRowSTO) {

        lessonReportRowService.updateLessonReportRowAndCompletedTask(lessonReportRowSTO);
        return new ResponseEntity<>("Successful update", HttpStatus.OK);
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
//
//    @GetMapping("/start")
//    public ResponseEntity<String> startMonitor() {
//        monitor.start();
//        return new ResponseEntity<>("Monitor started", HttpStatus.ACCEPTED);
//    }
//
//    @GetMapping("/stop")
//    public ResponseEntity<String> stopMonitor() {
//        monitor.stop();
//        return new ResponseEntity<>("Monitor stopped", HttpStatus.ACCEPTED);
//    }


}
