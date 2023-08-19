package com.example.spacelab.service.impl;

import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.mapper.LiteratureMapper;
import com.example.spacelab.model.Course;
import com.example.spacelab.model.Literature;
import com.example.spacelab.model.dto.LiteratureDTO.LiteratureListDTO;
import com.example.spacelab.repository.CourseRepository;
import com.example.spacelab.repository.LiteratureRepository;
import com.example.spacelab.service.LiteratureService;
import com.example.spacelab.service.specification.LiteratureSpecifications;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.util.LiteratureType;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
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
    public List<LiteratureListDTO> getLiterature() {
        return literatureRepository.findAll()
                .stream()
                .map(literatureMapper::fromLiteratureToDTO)
                .toList();
    }

    @Override
    public List<LiteratureListDTO> getLiterature(Pageable pageable) {
        return literatureRepository.findAll(pageable)
                .stream()
                .map(literatureMapper::fromLiteratureToDTO)
                .toList();
    }

    @Override
    public List<LiteratureListDTO> getLiterature(FilterForm filters, Pageable pageable) {
        Specification<Literature> spec = buildSpecificationFromFilters(filters);
        return literatureRepository.findAll(spec, pageable)
                .stream()
                .map(literatureMapper::fromLiteratureToDTO)
                .toList();
    }

    @Override
    public void verifyLiterature(Long id) {
        Literature lit = literatureRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Literature not found"));
        lit.setIs_verified(true);
        literatureRepository.save(lit);
    }

    @Override
    public LiteratureListDTO getLiteratureById(Long id) {
        Literature literature = literatureRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Literature not found"));
        return literatureMapper.fromLiteratureToDTO(literature);
    }

    @Override
    public LiteratureListDTO createNewLiterature(LiteratureListDTO dto) {
        Literature literature = literatureMapper.fromDTOToLiterature(dto);
        if(literature.getIs_verified() == null || !literature.getIs_verified()) literature.setIs_verified(false);
        literature = literatureRepository.save(literature);
        return literatureMapper.fromLiteratureToDTO(literature);
    }


    @Override
    public LiteratureListDTO editLiterature(LiteratureListDTO dto) {
        Literature literature = literatureMapper.fromDTOToLiterature(dto);
        literature = literatureRepository.save(literature);
        return literatureMapper.fromLiteratureToDTO(literature);
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
