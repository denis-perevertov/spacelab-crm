package com.example.spacelab.service.impl;

import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.model.lesson.Lesson;
import com.example.spacelab.repository.LessonRepository;
import com.example.spacelab.service.LessonService;
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
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    @Override
    public List<Lesson> getLesson() {
        return lessonRepository.findAll();
    }

    @Override
    public Page<Lesson> getLesson(Pageable pageable) {
        return lessonRepository.findAll(pageable);
    }

    @Override
    public Page<Lesson> getLesson(FilterForm filters, Pageable pageable) {
        Specification<Lesson> spec = buildSpecificationFromFilters(filters);
        return lessonRepository.findAll(spec, pageable);
    }

    @Override
    public Lesson getLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        return lesson;
    }

    @Override
    public Lesson createNewLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson editLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    @Override
    public void deleteLessonById(Long id) {
        lessonRepository.deleteById(id);
    }

    @Override
    public Specification<Lesson> buildSpecificationFromFilters(FilterForm filters) {
//        String nameAuthorInput = filters.getName();
//        Long courseID = filters.getCourse();
//        String typeString = filters.getType();
//        String keywords = filters.getKeywords();
//
//        Course course = (courseID == null) ? null : courseRepository.getReferenceById(courseID);
//        LiteratureType type = (typeString == null) ? null : LiteratureType.valueOf(typeString);
//
//        Specification<Lesson> spec = Specification.where(
//                LiteratureSpecifications.hasNameOrAuthorLike(nameAuthorInput)
//                        .and(LiteratureSpecifications.hasCourse(course))
//                        .and(LiteratureSpecifications.hasType(type))
//                        .and(LiteratureSpecifications.hasKeywordsLike(keywords))
//        );
        Specification<Lesson> spec = Specification.where(null);
        return spec;
    }
}