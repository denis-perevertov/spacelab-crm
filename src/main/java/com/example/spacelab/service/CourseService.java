package com.example.spacelab.service;

import com.example.spacelab.model.course.Course;
import com.example.spacelab.dto.CourseDTO;
import com.example.spacelab.util.FilterForm;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService extends EntityFilterService<Course> {

    List<CourseDTO> getCourses();
    List<CourseDTO> getCourses(Pageable pageable);
    List<CourseDTO> getCourses(FilterForm filters, Pageable pageable);
    CourseDTO getCourseById(Long id);
    CourseDTO createNewCourse(CourseDTO course);
    CourseDTO editCourse(CourseDTO course);
    void deleteCourseById(Long id);
}
