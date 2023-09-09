package com.example.spacelab.service.impl;

import com.example.spacelab.exception.LessonException;
import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.job.LessonMonitor;
import com.example.spacelab.model.lesson.Lesson;
import com.example.spacelab.model.lesson.LessonStatus;
import com.example.spacelab.repository.LessonRepository;
import com.example.spacelab.service.LessonService;
import com.example.spacelab.service.specification.LessonSpecifications;
import com.example.spacelab.util.FilterForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Log
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final LessonMonitor monitor;

    public LessonServiceImpl(@Autowired LessonRepository lessonRepository,
                             @Autowired @Lazy LessonMonitor monitor) {
        this.lessonRepository = lessonRepository;
        this.monitor = monitor;
    }

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
    public List<Lesson> getLessonsByAllowedCourses(Long... ids) {
        return lessonRepository.findAllByAllowedCourse(ids);
    }

    @Override
    public Page<Lesson> getLessonsByAllowedCourses(Pageable pageable, Long... ids) {
        return lessonRepository.findAllByAllowedCoursePage(pageable, ids);
    }

    @Override
    public Page<Lesson> getLessonsByAllowedCourses(FilterForm filters, Pageable pageable, Long... ids) {
        Specification<Lesson> spec = buildSpecificationFromFilters(filters).and(LessonSpecifications.hasCourseIDs(ids));
        return lessonRepository.findAll(spec, pageable);
    }

    @Override
    public Lesson getLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        return lesson;
    }

    @Override
    public Lesson createNewLesson(Lesson lesson) {
        if(lesson.getDatetime().isAfter(LocalDateTime.now()) && lesson.getStartsAutomatically())
            monitor.getLessons().add(lesson);
        return lessonRepository.save(lesson);
    }


    @Override
    public Lesson editLesson(Lesson lesson) {

        if(monitor.isMonitored(lesson) && !lesson.getStartsAutomatically()) monitor.removeFromMonitor(lesson);
        if(!monitor.isMonitored(lesson) && lesson.getStartsAutomatically()) monitor.addToMonitor(lesson);
        return lessonRepository.save(lesson);
    }

    @Override
    public void deleteLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lesson with this ID doesn't exist!"));
        if(lesson.getStatus().equals(LessonStatus.ACTIVE)) {
            log.warning("Attempt to delete a lesson already in progress");
            log.warning(lesson.toString());
            throw new RuntimeException("Cannot delete an active lesson!");
        }
        if(monitor.isMonitored(lesson)) monitor.removeFromMonitor(lesson);
        lessonRepository.delete(lesson);
    }

    @Override
    public void startLesson(Long id) {
        Lesson lesson = getLessonById(id);
        if(!lesson.getStatus().equals(LessonStatus.PLANNED)) throw new LessonException("Can't start an active/completed lesson!");
        lesson.setStatus(LessonStatus.ACTIVE);
        lessonRepository.save(lesson);
    }

    @Override
    public void completeLesson(Long id) {
        Lesson lesson = getLessonById(id);
        if(!lesson.getStatus().equals(LessonStatus.ACTIVE)) throw new LessonException("Can't complete a completed/planned lesson!");
        lesson.setStatus(LessonStatus.COMPLETED);
        lessonRepository.save(lesson);
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
