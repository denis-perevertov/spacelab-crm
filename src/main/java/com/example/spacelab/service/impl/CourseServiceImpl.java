package com.example.spacelab.service.impl;

import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.mapper.CourseMapper;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.dto.CourseDTO;
import com.example.spacelab.repository.AdminRepository;
import com.example.spacelab.repository.CourseRepository;
import com.example.spacelab.service.CourseService;
import com.example.spacelab.service.specification.CourseSpecifications;
import com.example.spacelab.util.FilterForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final AdminRepository adminRepository;
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    public List<CourseDTO> getCourses() {
        return courseRepository.findAll().stream()
                .map(courseMapper::fromCourseToDTO)
                .toList();
    }

    @Override
    public List<CourseDTO> getCourses(Pageable pageable) {
        return courseRepository.findAll(pageable).get()
                .map(courseMapper::fromCourseToDTO).toList();
    }

    @Override
    public List<CourseDTO> getCourses(FilterForm filters, Pageable pageable) {
        Specification<Course> spec = buildSpecificationFromFilters(filters);

        return courseRepository.findAll(spec, pageable).get()
                .map(courseMapper::fromCourseToDTO).toList();
    }

    @Override
    public Specification<Course> buildSpecificationFromFilters(FilterForm filters) {

        String name = filters.getName();
        String dateString = filters.getDate();
        Long mentorID = filters.getMentor();
        Long managerID = filters.getManager();
        Boolean active = filters.getActive();

        Admin mentor = (mentorID == null) ? null : adminRepository.getReferenceById(mentorID);
        Admin manager = (mentorID == null) ? null : adminRepository.getReferenceById(managerID);

        /*
            TODO
            фильтр по датам
        */

        Specification<Course> spec = Specification.where(
                CourseSpecifications.hasNameLike(name)
                        .and(CourseSpecifications.hasMentor(mentor))
                        .and(CourseSpecifications.hasManager(manager))
                        .and(CourseSpecifications.hasActive(active))
        );
        return spec;
    }

    @Override
    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        return courseMapper.fromCourseToDTO(course);
    }

    @Override
    public CourseDTO createNewCourse(CourseDTO dto) {
        Course course = courseMapper.fromDTOToCourse(dto);
        course = courseRepository.save(course);
        return courseMapper.fromCourseToDTO(course);
    }

    @Override
    public CourseDTO editCourse(CourseDTO dto) {
        Course course = courseMapper.fromDTOToCourse(dto);
        course = courseRepository.save(course);
        return courseMapper.fromCourseToDTO(course);
    }

    @Override
    public void deleteCourseById(Long id) {
        courseRepository.deleteById(id);
    }
}
