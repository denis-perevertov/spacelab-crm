package com.example.spacelab.service.impl;

import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.model.lesson.Lesson;
import com.example.spacelab.repository.LessonRepository;
import com.example.spacelab.util.FilterForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@SpringBootTest
@SpringJUnitConfig
@AutoConfigureMockMvc
class LessonServiceImplTest {
    @Mock
    private LessonRepository lessonRepository;

    private LessonServiceImpl lessonService;

    @BeforeEach
    void setUp() {
//        lessonService = new LessonServiceImpl(lessonRepository);
    }

    @Test
    public void testGetLesson() {
        List<Lesson> mockLessonList = new ArrayList<>();
        mockLessonList.add(new Lesson());
        mockLessonList.add(new Lesson());

        when(lessonRepository.findAll()).thenReturn(mockLessonList);

        List<Lesson> result = lessonService.getLesson();

        assertEquals(2, result.size());
    }

    @Test
    public void testGetLessonById() {
        Long lessonId = 1L;
        Lesson mockLesson = new Lesson();
        mockLesson.setId(lessonId);

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(mockLesson));

        Lesson result = lessonService.getLessonById(lessonId);

        assertEquals(lessonId, result.getId());
    }

    @Test
    public void testGetLessonById_ThrowsException() {
        Long lessonId = 1L;

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> lessonService.getLessonById(lessonId));
    }

    @Test
    public void testCreateNewLesson() {
        Lesson mockLesson = new Lesson();

        when(lessonRepository.save(any(Lesson.class))).thenReturn(mockLesson);

        Lesson result = lessonService.createNewLesson(mockLesson);

        assertEquals(mockLesson, result);
    }

    @Test
    public void testEditLesson() {
        Lesson mockLesson = new Lesson();

        when(lessonRepository.save(any(Lesson.class))).thenReturn(mockLesson);

        Lesson result = lessonService.editLesson(mockLesson);

        assertEquals(mockLesson, result);
    }

    @Test
    public void testDeleteLessonById() {
        Long lessonId = 1L;

        lessonService.deleteLessonById(lessonId);

        verify(lessonRepository, times(1)).deleteById(lessonId);
    }

//    @Test
//    public void testBuildSpecificationFromFilters() {
//        FilterForm filters = new FilterForm(); // Передайте фильтры по вашей логике
//
//        Specification<Lesson> result = lessonService.buildSpecificationFromFilters(filters);
//
//        //To Do
//    }
}