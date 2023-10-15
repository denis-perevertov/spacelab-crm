package com.example.spacelab.service.impl;

import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.mapper.CourseMapper;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.course.CourseStatus;
import com.example.spacelab.repository.AdminRepository;
import com.example.spacelab.repository.CourseRepository;
import com.example.spacelab.service.CourseService;
import com.example.spacelab.service.specification.CourseSpecifications;
import com.example.spacelab.util.FilterForm;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Data
@Log
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final AdminRepository adminRepository;
    private final CourseRepository courseRepository;

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
    public List<Course> getAllowedCourses(Long... ids) {
        return courseRepository.findAllowedCourses(ids);
    }

    @Override
    public Page<Course> getAllowedCourses(Pageable pageable, Long... ids) {
        return courseRepository.findAllowedCoursesPage(pageable, ids);
    }

    @Override
    public Page<Course> getAllowedCourses(FilterForm filters, Pageable pageable, Long... ids) {
        Specification<Course> spec = buildSpecificationFromFilters(filters).and(CourseSpecifications.hasCourseIDs(ids));
        return courseRepository.findAll(spec, pageable);
    }

    @Override
    public Page<Course> getCoursesByName(String name, Pageable pageable) {
        FilterForm filters = new FilterForm();
        filters.setName(name);
        Specification<Course> spec = buildSpecificationFromFilters(filters);
        return courseRepository.findAll(spec, pageable);
    }

    @Override
    public Specification<Course> buildSpecificationFromFilters(FilterForm filters) {

        log.info("Building specification from filters: " + filters);

        String name = filters.getName();
        String dateString = filters.getDate();
        String beginDateString = filters.getBegin();
        String endDateString = filters.getEnd();
        Long mentorID = filters.getMentor();
        Long managerID = filters.getManager();
        String status = filters.getStatus();

        CourseStatus courseStatus = (status == null || status.isEmpty()) ? null : CourseStatus.valueOf(status);

        Admin mentor = (mentorID == null) ? null : adminRepository.getReferenceById(mentorID);
        Admin manager = (managerID == null) ? null : adminRepository.getReferenceById(managerID);

        LocalDate beginDate = (beginDateString != null && !beginDateString.isEmpty()) ? LocalDate.parse(beginDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null;
        LocalDate endDate = (endDateString != null && !endDateString.isEmpty()) ? LocalDate.parse(endDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null;

//        LocalDate beginDate = null;
//        LocalDate endDate = null;
//        if(dateString != null && !dateString.isEmpty()) {
//            try {
//                String[] dates = dateString.split(" - ");
//                beginDate = LocalDate.parse(dates[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//                endDate = LocalDate.parse(dates[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//            } catch (Exception e) {
//                log.warning("Error during date parsing");
//            }
//        }

        Specification<Course> spec = Specification.where(
                        CourseSpecifications.hasNameLike(name)
                        .and(CourseSpecifications.hasMentor(mentor))
                        .and(CourseSpecifications.hasManager(manager))
                        .and(CourseSpecifications.hasStatus(courseStatus)
                        .and(CourseSpecifications.hasDatesBetween(beginDate, endDate)))
        );
        return spec;
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    @Override
    public Course createNewCourse(Course course) {
        course.setStatus(CourseStatus.ACTIVE);
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
