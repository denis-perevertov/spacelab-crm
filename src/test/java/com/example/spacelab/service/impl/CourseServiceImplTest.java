package com.example.spacelab.service.impl;

import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.repository.AdminRepository;
import com.example.spacelab.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringBootTest
@SpringJUnitConfig
@AutoConfigureMockMvc
class CourseServiceImplTest {


    @Mock
    private AdminRepository adminRepository;

    @Mock
    private CourseRepository courseRepository;

    private CourseServiceImpl courseService;

    @BeforeEach
    void setUp() {
        courseService = new CourseServiceImpl(adminRepository, courseRepository);
    }

    @Test
    public void testGetCourses() {
        List<Course> mockCourseList = new ArrayList<>();
        mockCourseList.add(new Course());
        mockCourseList.add(new Course());
        when(courseRepository.findAll()).thenReturn(mockCourseList);

        List<Course> result = courseService.getCourses();

        assertEquals(2, result.size());
    }

    @Test
    public void testGetCoursesById() {
        Long courseId = 1L;
        Course mockCourse = new Course();
        mockCourse.setId(courseId);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(mockCourse));

        Course result = courseService.getCourseById(courseId);

        assertEquals(courseId, result.getId());
    }

    @Test
    public void testGetCoursesById_ThrowsException() {
        Long courseId = 1L;

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courseService.getCourseById(courseId));
    }

    @Test
    public void testCreateNewCourse() {
        Course mockCourse = new Course();
        when(courseRepository.save(any(Course.class))).thenReturn(mockCourse);

        Course result = courseService.createNewCourse(mockCourse);

        assertEquals(mockCourse, result);
    }

    @Test
    public void testEditCourse() {
        Course mockCourse = new Course();
        when(courseRepository.save(any(Course.class))).thenReturn(mockCourse);

        Course result = courseService.editCourse(mockCourse);

        assertEquals(mockCourse, result);
    }

    @Test
    public void testDeleteCourseById() {
        Long courseId = 1L;
        courseService.deleteCourseById(courseId);

        verify(courseRepository, times(1)).deleteById(courseId);
    }

}