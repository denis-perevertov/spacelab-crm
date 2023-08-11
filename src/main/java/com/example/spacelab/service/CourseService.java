package com.example.spacelab.service;

import com.example.spacelab.model.Course;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CourseService {

    List<Course> getCourses();
    Course getCourseById(Long id);
    Course createNewCourse(Course course);
    Course editCourse(Course course);
    void deleteCourseById(Long id);
}
