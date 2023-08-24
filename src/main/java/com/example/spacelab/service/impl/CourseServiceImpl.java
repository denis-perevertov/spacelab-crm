package com.example.spacelab.service.impl;

import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.mapper.CourseMapper;
import com.example.spacelab.model.Admin;
import com.example.spacelab.model.Course;
import com.example.spacelab.repository.AdminRepository;
import com.example.spacelab.repository.CourseRepository;
import com.example.spacelab.service.CourseService;
import com.example.spacelab.service.specification.CourseSpecifications;
import com.example.spacelab.util.FilterForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
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
    public List<Course> getCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Page<Course> getCourses(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    @Override
    public Page<Course> getCourses(FilterForm filters, Pageable pageable) {
        Specification<Course> spec = buildSpecificationFromFilters(filters);

        return courseRepository.findAll(spec, pageable);
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
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    @Override
    public Course createNewCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course editCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public void deleteCourseById(Long id) {
        courseRepository.deleteById(id);
    }
}
