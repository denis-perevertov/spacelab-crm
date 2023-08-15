package com.example.spacelab.mapper;

import com.example.spacelab.model.Course;
import com.example.spacelab.model.dto.CourseDTO;
import org.springframework.stereotype.Component;

@Component
public class CourseDTOMapper {

    public CourseDTO fromCourseToDTO(Course course) {
        CourseDTO dto = new CourseDTO();

        dto.setId(course.getId());
        dto.setBegin_date(course.getBeginningDate());
        dto.setEnd_date(course.getEndDate());
//        dto.setStatus(course.getStatus());

        /*
            TODO
            - get mentor dto
            - get manager dto
        */

        return dto;
    }

    public Course fromDTOToCourse(CourseDTO dto) {

        return new Course();
    }
}
