package com.example.spacelab.controller;

import com.example.spacelab.mapper.LessonMapper;
import com.example.spacelab.model.lesson.Lesson;
import com.example.spacelab.dto.lesson.LessonInfoDTO;
import com.example.spacelab.dto.lesson.LessonListDTO;
import com.example.spacelab.dto.lesson.LessonReportRowSaveDTO;
import com.example.spacelab.dto.lesson.LessonSaveBeforeStartDTO;
import com.example.spacelab.service.LessonReportRowService;
import com.example.spacelab.service.LessonService;
import com.example.spacelab.util.FilterForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Log
@RequiredArgsConstructor
@RequestMapping("/lessons")
public class LessonController {

    private final LessonReportRowService lessonReportRowService;
    private final LessonService lessonService;
    private final LessonMapper mapper;

    @GetMapping
    public ResponseEntity<Page<LessonListDTO>> getLesson(FilterForm filters,
                                                                 @RequestParam(required = false) Integer page,
                                                                 @RequestParam(required = false) Integer size) {
        Page<Lesson> litList = lessonService.getLesson(filters, PageRequest.of(page, size));
        Page<LessonListDTO> dtoList = mapper.pageLessonToPageLessonListDTO(litList);
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonInfoDTO> getLessonById(@PathVariable Long id) {
        LessonInfoDTO less = mapper.fromLessonToLessonInfoDTO(lessonService.getLessonById(id));
        return new ResponseEntity<>(less, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createNewLessonBeforeStart(@Valid @RequestBody LessonSaveBeforeStartDTO lesson) {
        lessonService.createNewLesson(mapper.BeforeStartDTOtoLesson(lesson));
        return new ResponseEntity<>("Successful save", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editLessonBeforeStart(@PathVariable Long id, @Valid @RequestBody LessonSaveBeforeStartDTO lesson) {
        lessonService.editLesson(mapper.BeforeStartDTOtoLesson(lesson));
        return new ResponseEntity<>("Successful update", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLessonById(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> saveLessonReportRowAfterStart(@Valid @RequestBody LessonReportRowSaveDTO lessonReportRowSTO) {
       lessonReportRowService.updateLessonReportRowAndCompletedTask(lessonReportRowSTO);
        return new ResponseEntity<>("Successful update", HttpStatus.OK);
    }



}
