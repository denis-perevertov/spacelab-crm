package com.example.spacelab.service;

import com.example.spacelab.model.Course;
import com.example.spacelab.model.dto.CourseDTO.CourseListDTO;
import com.example.spacelab.util.FilterForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService extends EntityService<Course> {

    List<Course> getCourses();
    Page<Course> getCourses(Pageable pageable);
    Page<Course> getCourses(FilterForm filters, Pageable pageable);
    Course getCourseById(Long id);
    Course createNewCourse(Course course);
    Course editCourse(Course course);
    void deleteCourseById(Long id);
}
