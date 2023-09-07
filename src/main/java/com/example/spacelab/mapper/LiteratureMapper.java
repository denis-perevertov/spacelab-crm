package com.example.spacelab.mapper;

import com.example.spacelab.dto.literature.*;
import com.example.spacelab.exception.MappingException;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.literature.Literature;
import com.example.spacelab.service.CourseService;
import com.example.spacelab.model.literature.LiteratureType;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Log
@RequiredArgsConstructor
public class LiteratureMapper {
    private final CourseService courseService;


    public LiteratureListDTO fromLiteratureToListDTO(Literature literature) {
        LiteratureListDTO dto = new LiteratureListDTO();

        try {
            dto.setId(literature.getId());
            dto.setName(literature.getName());

            Course course = literature.getCourse();
            if (course != null) {
                dto.setCourseId(course.getId());
                dto.setCourseName(course.getName());
            }

            dto.setType(literature.getType());
            dto.setAuthor_name(literature.getAuthor());
            dto.setKeywords(literature.getKeywords());
            dto.setResource_link(literature.getResource_link());
        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("DTO: " + dto);
            throw new MappingException(e.getMessage());
        }

        return dto;
    }


    public LiteratureSelectDTO fromLiteratureToSelectDTO(Literature literature) {
        LiteratureSelectDTO dto = new LiteratureSelectDTO();
        dto.setId(literature.getId());
        dto.setName(literature.getName());
        return dto;
    }


    public List<LiteratureListDTO> fromListLiteraturetoListDTOList(List<Literature> literatureList) {
        List<LiteratureListDTO> dtoList = new ArrayList<>();
        for (Literature literature : literatureList) {
            dtoList.add(fromLiteratureToListDTO(literature));
        }
        return dtoList;
    }

    public List<LiteratureSelectDTO> fromListLiteraturetoListSelectList(List<Literature> literatureList) {
        List<LiteratureSelectDTO> dtoList = new ArrayList<>();
        for (Literature literature : literatureList) {
            dtoList.add(fromLiteratureToSelectDTO(literature));
        }
        return dtoList;
    }

    public Page<LiteratureListDTO> fromPageLiteraturetoPageDTOList(Page<Literature> literaturePage) {
        List<LiteratureListDTO> dtoList = fromListLiteraturetoListDTOList(literaturePage.getContent());
        return new PageImpl<>(dtoList, literaturePage.getPageable(), literaturePage.getTotalElements());
    }

    public Page<LiteratureSelectDTO> fromPageLiteraturetoPageSelectList(Page<Literature> literaturePage) {
        List<LiteratureSelectDTO> dtoList = fromListLiteraturetoListSelectList(literaturePage.getContent());
        return new PageImpl<>(dtoList, literaturePage.getPageable(), literaturePage.getTotalElements());
    }

    public Literature fromDTOToLiterature(LiteratureListDTO dto) {
        Literature literature = new Literature();

        try {
            literature.setId(dto.getId());
            literature.setName(dto.getName());

            Course course = new Course();
            course.setName(dto.getCourseName());
            literature.setCourse(course);

            literature.setType(dto.getType());
            literature.setAuthor(dto.getAuthor_name());
            literature.setKeywords(dto.getKeywords());
            literature.setResource_link(dto.getResource_link());
        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("Entity: " + literature);
            throw new MappingException(e.getMessage());
        }


        return literature;
    }

    public LiteratureInfoDTO fromLiteraturetoInfoDTO(Literature literature) {
        LiteratureInfoDTO dto = new LiteratureInfoDTO();

        try {
            dto.setId(literature.getId());
            dto.setName(literature.getName());
            dto.setCourseId(literature.getCourse().getId());
            dto.setCourseName(literature.getCourse().getName());
            dto.setType(literature.getType());
            dto.setAuthor_name(literature.getAuthor());
            dto.setKeywords(literature.getKeywords());
            dto.setResource_link(literature.getResource_link());
            dto.setDescription(literature.getDescription());
            dto.setImg(literature.getImg());
        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("DTO: " + dto);
            throw new MappingException(e.getMessage());
        }

        return dto;
    }

    public LiteratureCardDTO fromLiteratureToCardDTO(Literature literature) {
        LiteratureCardDTO dto = new LiteratureCardDTO();

        try {
            dto.setId(literature.getId());
            dto.setName(literature.getName());
            dto.setCourseId(literature.getCourse().getId());
            dto.setCourseName(literature.getCourse().getName());
            dto.setType(literature.getType());
            dto.setAuthor_name(literature.getAuthor());
            dto.setKeywords(literature.getKeywords());
            dto.setResource_link(literature.getResource_link());
            dto.setDescription(literature.getDescription());
            dto.setImg(literature.getImg());
        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("DTO: " + dto);
            throw new MappingException(e.getMessage());
        }


        return dto;
    }

    public Literature fromLiteratureSaveDTOtoLiterature (LiteratureSaveDTO dto) {
        Literature entity = new Literature();

        try {
            entity.setId(dto.getId());
            entity.setName(dto.getName());

            Course course = courseService.getCourseById(dto.getCourseID());
            entity.setCourse(course);

            entity.setType(LiteratureType.valueOf(dto.getType()));
            entity.setAuthor((dto.getAuthor_name() != null && !dto.getAuthor_name().isEmpty()) ? dto.getAuthor_name() : "Unknown Author");
            entity.setKeywords(dto.getKeywords());
            entity.setDescription(dto.getDescription());
            entity.setResource_link(dto.getResource_link());
        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("Entity: " + entity);
            throw new MappingException(e.getMessage());
        }


        return entity;
    }
}
