package com.example.spacelab.service.impl;

import com.example.spacelab.mapper.LiteratureMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.literature.Literature;
import com.example.spacelab.model.literature.LiteratureType;
import com.example.spacelab.repository.CourseRepository;
import com.example.spacelab.repository.LiteratureRepository;
import com.example.spacelab.util.FilterForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    class LiteratureServiceImplTest {

        @Mock
        private CourseRepository courseRepository;

        @Mock
        private LiteratureRepository literatureRepository;

        @Mock
        private LiteratureMapper literatureMapper;

        private LiteratureServiceImpl literatureService;

        @BeforeEach
        void setUp() {
            literatureService = new LiteratureServiceImpl(courseRepository, literatureRepository, literatureMapper);
        }

        @Test
        public void testGetLiterature() {
            List<Literature> mockLiteratureList = new ArrayList<>();
            mockLiteratureList.add(new Literature());
            mockLiteratureList.add(new Literature());

            when(literatureRepository.findAll()).thenReturn(mockLiteratureList);

            List<Literature> result = literatureService.getLiterature();

            assertEquals(2, result.size());
        }

        @Test
        public void testGetLiteratureById() {
            Long literatureId = 1L;
            Literature mockLiterature = new Literature();
            mockLiterature.setId(literatureId);

            when(literatureRepository.findById(literatureId)).thenReturn(Optional.of(mockLiterature));

            Literature result = literatureService.getLiteratureById(literatureId);

            assertEquals(literatureId, result.getId());
        }

        @Test
        public void testGetLiteratureById_ThrowsException() {
            Long literatureId = 1L;

            when(literatureRepository.findById(literatureId)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> literatureService.getLiteratureById(literatureId));
        }

        @Test
        public void testCreateNewLiterature() {
            Literature mockLiterature = new Literature();

            when(literatureRepository.save(any(Literature.class))).thenReturn(mockLiterature);

            Literature result = literatureService.createNewLiterature(mockLiterature);

            assertEquals(mockLiterature, result);
        }

        @Test
        public void testEditLiterature() {
            Literature mockLiterature = new Literature();

            when(literatureRepository.save(any(Literature.class))).thenReturn(mockLiterature);

            Literature result = literatureService.editLiterature(mockLiterature);

            assertEquals(mockLiterature, result);
        }

        @Test
        public void testDeleteLiteratureById() {
            Long literatureId = 1L;

            literatureService.deleteLiteratureById(literatureId);

            verify(literatureRepository, times(1)).deleteById(literatureId);
        }

        @Test
        public void testBuildSpecificationFromFilters() {

            FilterForm filters = new FilterForm(); // Передайте фильтры по вашей логике

            Specification<Literature> result = literatureService.buildSpecificationFromFilters(filters);

            // Здесь вы можете добавить утверждения, связанные с созданным Specification.
        }
    }