package com.example.spacelab.service;

import com.example.spacelab.model.Course;
import com.example.spacelab.model.dto.CourseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CourseService {

    List<Course> getCourses();
    List<CourseDTO> getCoursesByPage(Pageable pageable);
    Course getCourseById(Long id);
    CourseDTO getCourseDTOById(Long id);
    Course createNewCourse(Course course);
    Course createNewCourse(CourseDTO course);
    Course editCourse(Course course);
    void deleteCourseById(Long id);
}
