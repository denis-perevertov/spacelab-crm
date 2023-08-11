package com.example.spacelab.service.impl;

import com.example.spacelab.model.Course;
import com.example.spacelab.repository.CourseRepository;
import com.example.spacelab.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    public List<Course> getCourses() {
        return null;
    }

    @Override
    public Course getCourseById(Long id) {
        return null;
    }

    @Override
    public Course createNewCourse(Course course) {
        return null;
    }

    @Override
    public Course editCourse(Course course) {
        return null;
    }

    @Override
    public void deleteCourseById(Long id) {

    }
}
