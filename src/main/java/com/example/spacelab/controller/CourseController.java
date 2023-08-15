package com.example.spacelab.controller;

import com.example.spacelab.model.Course;
import com.example.spacelab.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
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
    private List<Course> getCourses() {
        return courseService.getCourses();
    }

    @GetMapping("/{id}")
    private Course getCourse(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    @PostMapping
    private ResponseEntity<String> createNewCourse(@RequestBody Course course) {
        return new ResponseEntity<>("New course created", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    private ResponseEntity<String> editCourse(@PathVariable Long id, @RequestBody Course course) {
        return new ResponseEntity<>("Course edited", HttpStatus.CREATED);
    }
//
//    @PatchMapping("/{id}")
//    private ResponseEntity<String> editCourse2(@PathVariable Long id) {
//        return ResponseEntity.ok("lol");
//    }

    @DeleteMapping("/{id}")
    private ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourseById(id);
        return new ResponseEntity<>("Course deleted", HttpStatus.OK);
    }

}
