package com.example.spacelab.mapper;

import com.example.spacelab.model.Admin;
import com.example.spacelab.model.Course;
import com.example.spacelab.model.Lesson;
import com.example.spacelab.model.Literature;
import com.example.spacelab.model.dto.lesson.LessonListDTO;
import com.example.spacelab.model.dto.literature.LiteratureCardDTO;
import com.example.spacelab.model.dto.literature.LiteratureInfoDTO;
import com.example.spacelab.model.dto.literature.LiteratureListDTO;
import com.example.spacelab.model.dto.literature.LiteratureSaveDTO;
import com.example.spacelab.service.CourseService;
import com.example.spacelab.util.LiteratureType;
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
public class LessonMapper {
    public static LessonListDTO  fromLessonToLessonListDTO(Lesson lesson) {
        LessonListDTO dto = new LessonListDTO();
        dto.setId(lesson.getId());
        dto.setDatetime(lesson.getDatetime());

        Course course = lesson.getCourse();
        if (course != null) {
            dto.setCourseId(course.getId());
            dto.setCourseName(course.getName());
        }

        dto.setLink(lesson.getLink());
        dto.setStatus(lesson.getStatus().toString());

        Admin mentor = lesson.getMentor();
        if (mentor != null) {
            dto.setMentorId(mentor.getId());
            dto.setMentorName(mentor.getFirstName()+" "+mentor.getLastName());
        }

        Admin manager = lesson.getManager();
        if (manager != null) {
            dto.setManagerId(manager.getId());
            dto.setManagerName(manager.getFirstName()+" "+manager.getLastName());
        }

        dto.setPresentStudentsQuantity(lesson.getLessonReport().getRows().stream().filter(row -> row.getWasPresent()).count()+
                "/ "+lesson.getLessonReport().getRows().size());

        return dto;
    }

    public static List<LessonListDTO> lessonsToLessonListDTOs(List<Lesson> lessons) {
        List<LessonListDTO> dtos = new ArrayList<>();
        for (Lesson lesson : lessons) {
            dtos.add(fromLessonToLessonListDTO(lesson));
        }
        return dtos;
    }

    public static Page<LessonListDTO> pageLessonToPageLessonListDTO(Page<Lesson> lessonPage) {
        List<Lesson> lessonList = lessonPage.getContent();
        List<LessonListDTO> lessonDTOList = lessonsToLessonListDTOs(lessonList);

        return new PageImpl<>(lessonDTOList, lessonPage.getPageable(), lessonPage.getTotalElements());
    }
}
