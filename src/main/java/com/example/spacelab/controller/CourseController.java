package com.example.spacelab.controller;

import com.example.spacelab.mapper.CourseMapper;
import com.example.spacelab.model.Course;
import com.example.spacelab.model.dto.CourseDTO.CourseInfoDTO;
import com.example.spacelab.model.dto.CourseDTO.CourseListDTO;
import com.example.spacelab.model.dto.CourseDTO.CourseSaveDTO;
import com.example.spacelab.service.CourseService;
import com.example.spacelab.util.FilterForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final CourseMapper mapper;


  private ResponseEntity<Page<CourseListDTO>> getCourses(FilterForm filters,
                                                           @RequestParam(required = false) Integer page,
                                                           @RequestParam(required = false) Integer size) {
      Page<Course> courseList = courseService.getCourses(filters, PageRequest.of(page, size));
      Page<CourseListDTO> courseListDTO = mapper.fromCoursePageToListDTOPage(courseList);
      return new ResponseEntity<>(courseListDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    private ResponseEntity<CourseInfoDTO> getCourse(@PathVariable Long id) {
        CourseInfoDTO course = mapper.fromCourseToInfoDTO(courseService.getCourseById(id));
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @PostMapping
    private ResponseEntity<String> createNewCourse(@Valid @RequestBody CourseSaveDTO dto) {
        courseService.createNewCourse(mapper.fromCardDTOtoCourse(dto));
        return new ResponseEntity<>("Course created", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    private ResponseEntity<String> editCourse(@PathVariable Long id, @Valid @RequestBody CourseSaveDTO dto) {
        courseService.createNewCourse(mapper.fromCardDTOtoCourse(dto));
        return new ResponseEntity<>("Course updated", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourseById(id);
        return new ResponseEntity<>("Course deleted", HttpStatus.OK);
    }

}
