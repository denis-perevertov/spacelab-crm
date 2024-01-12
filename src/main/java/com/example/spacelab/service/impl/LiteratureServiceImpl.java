package com.example.spacelab.service.impl;

import com.example.spacelab.dto.literature.LiteratureSaveDTO;
import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.mapper.LiteratureMapper;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.literature.Literature;
import com.example.spacelab.repository.CourseRepository;
import com.example.spacelab.repository.LiteratureRepository;
import com.example.spacelab.service.FileService;
import com.example.spacelab.service.LiteratureService;
import com.example.spacelab.service.specification.LiteratureSpecifications;
import com.example.spacelab.util.FilenameUtils;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.model.literature.LiteratureType;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LiteratureServiceImpl implements LiteratureService{

    private final CourseRepository courseRepository;
    private final LiteratureRepository literatureRepository;

    private final LiteratureMapper literatureMapper;

    @Qualifier("s3")
    private final FileService fileService;

    @Override
    public List<Literature> getLiterature() {
        log.info("Getting the list of all literature entities");
        return literatureRepository.findAll();
    }

    @Override
    public Page<Literature> getLiterature(Pageable pageable) {
        log.info("Getting the page of all lit.entities w/ pageable: {}", pageable);
        return literatureRepository.findAll(pageable);
    }

    @Override
    public Page<Literature> getLiterature(FilterForm filters, Pageable pageable) {
        log.info("Getting the page of all literature entities with pageable and filters: {}", filters);
        Specification<Literature> spec = buildSpecificationFromFilters(filters);
        return literatureRepository.findAll(spec, pageable);
    }

    @Override
    public List<Literature> getLiteratureByAllowedCourses(Long... ids) {
        log.info("Getting the list of all literature entities by allowed IDs: {}", (Object) ids);
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
        literature.setIs_verified(false);
        log.info("logging lit before save");
        log.info(literature.toString());
        return literatureRepository.save(literature);
    }

    @Override
    public Literature createNewLiterature(LiteratureSaveDTO saveRequest) throws IOException {
        Literature lit = literatureMapper.fromLiteratureSaveDTOtoLiterature(saveRequest);
        MultipartFile file = saveRequest.getResource_file();
        if(file != null && file.getSize() > 0) {
            String filename = FilenameUtils.generateFileName(file);
            fileService.saveFile(file, filename, "literature", "books");
            lit.setResource_link(filename);
        }
        MultipartFile thumbnail = saveRequest.getThumbnail();
        if(thumbnail != null && thumbnail.getSize() > 0) {
            String filename = FilenameUtils.generateFileName(thumbnail);
            fileService.saveFile(thumbnail, filename, "literature", "thumbnails");
            lit.setThumbnail(filename);
        }
        return literatureRepository.save(lit);
    }


    @Override
    public Literature editLiterature(LiteratureSaveDTO saveRequest) throws IOException {
        Literature lit = literatureMapper.fromLiteratureSaveDTOtoLiterature(saveRequest);
        MultipartFile file = saveRequest.getResource_file();
        if(file != null && file.getSize() > 0) {
            String filename = FilenameUtils.generateFileName(file);
            fileService.saveFile(file, filename, "literature", "books");
            lit.setResource_link(filename);
        }
        MultipartFile thumbnail = saveRequest.getThumbnail();
        if(thumbnail != null && thumbnail.getSize() > 0) {
            String filename = FilenameUtils.generateFileName(thumbnail);
            fileService.saveFile(thumbnail, filename, "literature", "thumbnails");
            lit.setThumbnail(filename);
        }
        else {
            lit.setThumbnail(null);
        }
        return literatureRepository.save(lit);
    }

    @Override
    public File getLiteratureFileById(Long id) throws IOException {
        String filename = getLiteratureById(id).getResource_link();
        return fileService.getFile(filename, "literature", "books");
    }

    @Override
    public void deleteLiteratureById(Long id) {
        literatureRepository.deleteById(id);
    }

    @Override
    public Specification<Literature> buildSpecificationFromFilters(FilterForm filters) {
        String nameAuthorInput = filters.getNameAndAuthor();
        Long courseID = filters.getCourse();
        String typeString = filters.getType();
        String keywords = filters.getKeywords();

        Boolean verified = filters.getVerified();

        Course course = (courseID == null) ? null : courseRepository.getReferenceById(courseID);
        LiteratureType type = (typeString == null) ? null : LiteratureType.valueOf(typeString);

        Specification<Literature> spec = Specification.where(
                LiteratureSpecifications.hasNameOrAuthorLike(nameAuthorInput)
                        .and(LiteratureSpecifications.hasCourse(course))
                        .and(LiteratureSpecifications.hasType(type))
                        .and(LiteratureSpecifications.hasKeywordsLike(keywords))
                        .and(LiteratureSpecifications.isVerified(verified))
        );

        return spec;
    }
}
