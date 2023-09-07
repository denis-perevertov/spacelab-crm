package com.example.spacelab.service;

import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.util.FilterForm;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService extends EntityFilterService<Course> {

    List<Course> getCourses();
    Page<Course> getCourses(Pageable pageable);
    Page<Course> getCourses(FilterForm filters, Pageable pageable);

    // filter by allowed courses
    List<Course> getAllowedCourses(Long... ids);
    Page<Course> getAllowedCourses(Pageable pageable, Long... ids);
    Page<Course> getAllowedCourses(FilterForm filters, Pageable pageable, Long... ids);

    Page<Course> getCoursesByName(String name, Pageable pageable);

    Course getCourseById(Long id);
    Course createNewCourse(Course course);
    Course editCourse(Course course);

    void deleteCourseById(Long id);
}
