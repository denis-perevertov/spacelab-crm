package com.example.spacelab.controller;

import com.example.spacelab.dto.lesson.LessonInfoDTO;
import com.example.spacelab.dto.lesson.LessonListDTO;
import com.example.spacelab.dto.lesson.LessonReportRowSaveDTO;
import com.example.spacelab.dto.lesson.LessonSaveBeforeStartDTO;
import com.example.spacelab.exception.ErrorMessage;
import com.example.spacelab.exception.ObjectValidationException;
import com.example.spacelab.mapper.LessonMapper;
import com.example.spacelab.model.lesson.Lesson;
import com.example.spacelab.service.LessonReportRowService;
import com.example.spacelab.service.LessonService;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.validator.LessonBeforeStartValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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



    //Получение списка уроков по фильтру и пагинации
    @Operation(description = "Get lesson list", summary = "Get lesson list", tags = {"Lesson"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('lesson.read.NO_ACCESS')")
    @GetMapping
    public ResponseEntity<Page<LessonListDTO>> getLesson(FilterForm filters,
                                                                 @RequestParam(required = false) Integer page,
                                                                 @RequestParam(required = false) Integer size) {
        Page<Lesson> litList = lessonService.getLesson(filters, PageRequest.of(page, size));
        Page<LessonListDTO> dtoList = mapper.pageLessonToPageLessonListDTO(litList);
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }



    //Получение урока по id
    @Operation(description = "Get lesson by id", summary = "Get lesson by id", tags = {"Lesson"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Lesson not found in DB", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('lesson.read.NO_ACCESS')")
    @GetMapping("/{id}")
    public ResponseEntity<LessonInfoDTO> getLessonById(@PathVariable Long id) {
        LessonInfoDTO less = mapper.fromLessonToLessonInfoDTO(lessonService.getLessonById(id));
        return new ResponseEntity<>(less, HttpStatus.OK);
    }



    //Создание урока
    @Operation(description = "Create new lesson", summary = "Create new lesson", tags = {"Lesson"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful save"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('lesson.write.NO_ACCESS')")
    @PostMapping
    public ResponseEntity<String> createNewLessonBeforeStart(@Valid @RequestBody LessonSaveBeforeStartDTO lesson, BindingResult bindingResult) {
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
    @Operation(description = "Edit lesson", summary = "Edit lesson", tags = {"Lesson"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful update"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "404", description = "Lesson not found in DB", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('lesson.edit.NO_ACCESS')")
    @PutMapping("/{id}")
    public ResponseEntity<String> editLessonBeforeStart(@PathVariable Long id, @Valid @RequestBody LessonSaveBeforeStartDTO lesson, BindingResult bindingResult) {
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
            @ApiResponse(responseCode = "404", description = "Lesson not found in DB", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('lesson.delete.NO_ACCESS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLessonById(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }



    //Сохранение ответа студента на уроке
    @Operation(description = "Start lesson", summary = "Start lesson", tags = {"Lesson"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful start"),
            @ApiResponse(responseCode = "404", description = "Lesson not found in DB", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('lesson.edit.NO_ACCESS')")
    @PostMapping("/update")
    public ResponseEntity<String> saveLessonReportRowAfterStart(@Valid @RequestBody LessonReportRowSaveDTO lessonReportRowSTO) {
       lessonReportRowService.updateLessonReportRowAndCompletedTask(lessonReportRowSTO);
        return new ResponseEntity<>("Successful update", HttpStatus.OK);
    }



}
