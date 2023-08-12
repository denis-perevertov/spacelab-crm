package com.example.spacelab.service.impl;

import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.mapper.CourseDTOMapper;
import com.example.spacelab.model.Course;
import com.example.spacelab.model.dto.CourseDTO;
import com.example.spacelab.repository.CourseRepository;
import com.example.spacelab.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseDTOMapper courseMapper;

    @Override
    public List<Course> getCourses() {
        return courseRepository.findAll();
    }

    @Override
    public List<CourseDTO> getCoursesByPage(Pageable pageable) {
        return courseRepository.findAll(pageable).get().map(courseMapper::fromCourseToDTO).toList();
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    @Override
    public CourseDTO getCourseDTOById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        return courseMapper.fromCourseToDTO(course);
    }

    @Override
    public Course createNewCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course createNewCourse(CourseDTO course) {
        return courseRepository.save(courseMapper.fromDTOToCourse(course));
    }

    @Override
    public Course editCourse(Course course) {
        return null;
    }

    @Override
    public void deleteCourseById(Long id) {
        courseRepository.deleteById(id);
    }
}
