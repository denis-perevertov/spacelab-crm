package com.example.spacelab.service;

import com.example.spacelab.model.lesson.Lesson;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.util.FilterForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LessonService extends EntityFilterService<Lesson>{
    List<Lesson> getLesson();
    Page<Lesson> getLesson(Pageable pageable);
    Page<Lesson> getLesson(FilterForm filters, Pageable pageable);

    List<Lesson> getLessonsByAllowedCourses(Long... ids);
    Page<Lesson> getLessonsByAllowedCourses(Pageable pageable, Long... ids);
    Page<Lesson> getLessonsByAllowedCourses(FilterForm filters, Pageable pageable, Long... ids);

    Lesson getLessonById(Long id);
    Lesson createNewLesson(Lesson lesson);
    Lesson editLesson(Lesson lesson);

    void deleteLessonById(Long id);

    void startLesson(Long id);
    void completeLesson(Long id);
}
