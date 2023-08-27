package com.example.spacelab.mapper;

import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.literature.Literature;
import com.example.spacelab.dto.literature.LiteratureCardDTO;
import com.example.spacelab.dto.literature.LiteratureInfoDTO;
import com.example.spacelab.dto.literature.LiteratureListDTO;
import com.example.spacelab.dto.literature.LiteratureSaveDTO;
import com.example.spacelab.service.CourseService;
import com.example.spacelab.model.literature.LiteratureType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LiteratureMapper {
    private final CourseService courseService;
    public LiteratureListDTO fromLiteratureToListDTO(Literature literature) {
        LiteratureListDTO dto = new LiteratureListDTO();
        dto.setId(literature.getId());
        dto.setName(literature.getName());

        Course course = literature.getCourse();
        if (course != null) {
            dto.setCourseId(course.getId());
            dto.setCourseName(course.getName());
        }

        dto.setType(literature.getType().toString());
        dto.setAuthor_name(literature.getAuthor());
        dto.setKeywords(literature.getKeywords());
        dto.setResource_link(literature.getResource_link());

        return new LiteratureListDTO();
    }
    public List<LiteratureListDTO> fromListLiteraturetoListDTOList(List<Literature> literatureList) {
        List<LiteratureListDTO> dtoList = new ArrayList<>();
        for (Literature literature : literatureList) {
            dtoList.add(fromLiteratureToListDTO(literature));
        }
        return dtoList;
    }

    public Page<LiteratureListDTO> fromPageLiteraturetoPageDTOList(Page<Literature> literaturePage) {
        List<LiteratureListDTO> dtoList = fromListLiteraturetoListDTOList(literaturePage.getContent());
        return new PageImpl<>(dtoList, literaturePage.getPageable(), literaturePage.getTotalElements());
    }

    public Literature fromDTOToLiterature(LiteratureListDTO dto) {
        Literature literature = new Literature();
        literature.setId(dto.getId());
        literature.setName(dto.getName());

        Course course = new Course();
        course.setName(dto.getCourseName());
        literature.setCourse(course);

        literature.setType(LiteratureType.valueOf(dto.getType()));
        literature.setAuthor(dto.getAuthor_name());
        literature.setKeywords(dto.getKeywords());
        literature.setResource_link(dto.getResource_link());
        return literature;
    }

    public LiteratureInfoDTO fromLiteraturetoInfoDTO(Literature literature) {
        LiteratureInfoDTO dto = new LiteratureInfoDTO();
        dto.setId(literature.getId());
        dto.setName(literature.getName());
        dto.setCourseId(literature.getCourse().getId());
        dto.setCourseName(literature.getCourse().getName());
        dto.setType(literature.getType().toString());
        dto.setAuthor_name(literature.getAuthor());
        dto.setKeywords(literature.getKeywords());
        dto.setDescription(literature.getDescription());
        dto.setResource_link(literature.getResource_link());
        return dto;
    }

    public LiteratureCardDTO fromLiteratureToCardDTO(Literature literature) {
        LiteratureCardDTO dto = new LiteratureCardDTO();
        dto.setId(literature.getId());
        dto.setName(literature.getName());
        dto.setCourseId(literature.getCourse().getId());
        dto.setCourseName(literature.getCourse().getName());
        dto.setType(literature.getType().toString());
        dto.setAuthor_name(literature.getAuthor());
        dto.setKeywords(literature.getKeywords());
        dto.setResource_link(literature.getResource_link());
        dto.setDescription(literature.getDescription());

        Map<Long, String> coursesMap = new HashMap<>();
        for (Course course : courseService.getCourses()){
            coursesMap.put(course.getId(), course.getName());
        }
        dto.setCourses(coursesMap);

        return dto;
    }

    public Literature fromLiteratureSaveDTOtoLiterature (LiteratureSaveDTO dto) {
        Literature entity = new Literature();
        entity.setId(dto.getId());
        entity.setName(dto.getName());

        Course course = courseService.getCourseById(dto.getId());
        entity.setCourse(course);

        entity.setType(LiteratureType.valueOf(dto.getType()));
        entity.setAuthor(dto.getAuthor_name());
        entity.setKeywords(dto.getKeywords());
        entity.setDescription(dto.getDescription());
        entity.setResource_link(dto.getResource_link());

        return entity;
    }
}
