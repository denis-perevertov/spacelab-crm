package com.example.spacelab.service;

import com.example.spacelab.dto.student.StudentLessonDisplayDTO;
import com.example.spacelab.model.lesson.Lesson;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.util.FilterForm;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
@Hidden
public interface LessonService extends EntityFilterService<Lesson>{
    List<Lesson> getLessons();
    Page<Lesson> getLessons(Pageable pageable);
    Page<Lesson> getLessons(FilterForm filters, Pageable pageable);

    List<Lesson> getLessonsByAllowedCourses(Long... ids);
    Page<Lesson> getLessonsByAllowedCourses(Pageable pageable, Long... ids);
    Page<Lesson> getLessonsByAllowedCourses(FilterForm filters, Pageable pageable, Long... ids);

    Lesson getLessonById(Long id);
    Lesson createNewLesson(Lesson lesson);
    Lesson editLesson(Lesson lesson);

    List<StudentLessonDisplayDTO> getStudentLessonDisplayData(Long id);

    void deleteLessonById(Long id);

    void startLesson(Long id);
    void completeLesson(Long id);

    void checkAutomaticLessonStart();

    long getCompletedLessonsCount();
}
