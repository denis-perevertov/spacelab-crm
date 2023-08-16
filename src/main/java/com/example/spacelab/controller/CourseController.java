package com.example.spacelab.controller;

import com.example.spacelab.model.Course;
import com.example.spacelab.model.dto.CourseDTO;
import com.example.spacelab.service.CourseService;
import com.example.spacelab.util.FilterForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Log
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    private ResponseEntity<List<CourseDTO>> getCourses(FilterForm filters,
                                                       @RequestParam(required = false) Integer page,
                                                       @RequestParam(required = false) Integer size) {
        List<CourseDTO> courseList = courseService.getCourses(filters, PageRequest.of(page, size));
        return new ResponseEntity<>(courseList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    private ResponseEntity<CourseDTO> getCourse(@PathVariable Long id) {
        CourseDTO course = courseService.getCourseById(id);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @PostMapping
    private ResponseEntity<CourseDTO> createNewCourse(@Valid @RequestBody CourseDTO dto) {
        CourseDTO course = courseService.createNewCourse(dto);
        return new ResponseEntity<>(course, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    private ResponseEntity<CourseDTO> editCourse(@PathVariable Long id, @Valid @RequestBody CourseDTO dto) {
        CourseDTO course = courseService.editCourse(dto);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourseById(id);
        return new ResponseEntity<>("Course deleted", HttpStatus.OK);
    }

}
