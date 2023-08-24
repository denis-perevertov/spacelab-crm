package com.example.spacelab.controller;

import com.example.spacelab.mapper.LessonMapper;
import com.example.spacelab.model.Lesson;
import com.example.spacelab.model.Literature;
import com.example.spacelab.model.dto.lesson.LessonListDTO;
import com.example.spacelab.model.dto.literature.LiteratureInfoDTO;
import com.example.spacelab.model.dto.literature.LiteratureListDTO;
import com.example.spacelab.model.dto.literature.LiteratureSaveDTO;
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

    private final LessonService lessonService;
    private final LessonMapper mapper;

    @GetMapping
    public ResponseEntity<Page<LessonListDTO>> getLiterature(FilterForm filters,
                                                                 @RequestParam(required = false) Integer page,
                                                                 @RequestParam(required = false) Integer size) {
        Page<Lesson> litList = lessonService.getLesson(filters, PageRequest.of(page, size));
        Page<LessonListDTO> dtoList = mapper.pageLessonToPageLessonListDTO(litList);
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<LiteratureInfoDTO> getLiteratureById(@PathVariable Long id) {
//        LiteratureInfoDTO lit = mapper.fromLiteraturetoInfoDTO(lessonService.getLiteratureById(id));
//        return new ResponseEntity<>(lit, HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}/verify")
//    public ResponseEntity<String> verifyLiterature(@PathVariable Long id) {
//        lessonService.verifyLiterature(id);
//        return new ResponseEntity<>("Verified successfully!", HttpStatus.OK);
//    }
//
//    @PostMapping
//    public ResponseEntity<String> createNewLiterature(@Valid @RequestBody LiteratureSaveDTO literature) {
//        lessonService.createNewLiterature(mapper.fromLiteratureSaveDTOtoLiterature(literature));
//        return new ResponseEntity<>("Successful save", HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<String> editLiterature(@PathVariable Long id, @Valid @RequestBody LiteratureSaveDTO literature) {
//        lessonService.editLiterature(mapper.fromLiteratureSaveDTOtoLiterature(literature));
//        return new ResponseEntity<>("Successful update", HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteLiterature(@PathVariable Long id) {
//        lessonService.deleteLiteratureById(id);
//        return new ResponseEntity<>("Deleted", HttpStatus.OK);
//    }
}
