package com.example.spacelab.service.impl;

import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.mapper.LiteratureMapper;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.literature.Literature;
import com.example.spacelab.repository.CourseRepository;
import com.example.spacelab.repository.LiteratureRepository;
import com.example.spacelab.service.LiteratureService;
import com.example.spacelab.service.specification.LiteratureSpecifications;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.model.literature.LiteratureType;
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
public class LiteratureServiceImpl implements LiteratureService{

    private final CourseRepository courseRepository;
    private final LiteratureRepository literatureRepository;

    private final LiteratureMapper literatureMapper;

    @Override
    public List<Literature> getLiterature() {
        return literatureRepository.findAll();
    }

    @Override
    public Page<Literature> getLiterature(Pageable pageable) {
        return literatureRepository.findAll(pageable);
    }

    @Override
    public Page<Literature> getLiterature(FilterForm filters, Pageable pageable) {
        Specification<Literature> spec = buildSpecificationFromFilters(filters);
        return literatureRepository.findAll(spec, pageable);
    }

    @Override
    public List<Literature> getLiteratureByAllowedCourses(Long... ids) {
        return literatureRepository.findAllByAllowedCourse(ids);
    }

    @Override
    public Page<Literature> getLiteratureByAllowedCourses(Pageable pageable, Long... ids) {
        return literatureRepository.findAllByAllowedCoursePage(pageable, ids);
    }

    @Override
    public Page<Literature> getLiteratureByAllowedCourses(FilterForm filters, Pageable pageable, Long... ids) {
        Specification<Literature> spec = buildSpecificationFromFilters(filters).and(LiteratureSpecifications.hasCourseIDs(ids));
        return literatureRepository.findAll(spec, pageable);
    }

    @Override
    public Page<Literature> getLiteratureByName(String name, Pageable pageable) {
        FilterForm filters = new FilterForm();
        filters.setName(name);
        Specification<Literature> spec = buildSpecificationFromFilters(filters);
        return literatureRepository.findAll(spec, pageable);
    }

    @Override
    public void verifyLiterature(Long id) {
        Literature lit = literatureRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Literature not found"));
        lit.setIs_verified(true);
        literatureRepository.save(lit);
    }

    @Override
    public Literature getLiteratureById(Long id) {
        Literature literature = literatureRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Literature not found"));
        return literature;
    }

    @Override
    public Literature createNewLiterature(Literature literature) {
        return literatureRepository.save(literature);
    }


    @Override
    public Literature editLiterature(Literature literature) {
        return literatureRepository.save(literature);
    }

    @Override
    public void deleteLiteratureById(Long id) {
        literatureRepository.deleteById(id);
    }

    @Override
    public Specification<Literature> buildSpecificationFromFilters(FilterForm filters) {
        String nameAuthorInput = filters.getName();
        Long courseID = filters.getCourse();
        String typeString = filters.getType();
        String keywords = filters.getKeywords();

        Course course = (courseID == null) ? null : courseRepository.getReferenceById(courseID);
        LiteratureType type = (typeString == null) ? null : LiteratureType.valueOf(typeString);

        Specification<Literature> spec = Specification.where(
                LiteratureSpecifications.hasNameOrAuthorLike(nameAuthorInput)
                        .and(LiteratureSpecifications.hasCourse(course))
                        .and(LiteratureSpecifications.hasType(type))
                        .and(LiteratureSpecifications.hasKeywordsLike(keywords))
        );

        return spec;
    }
}
