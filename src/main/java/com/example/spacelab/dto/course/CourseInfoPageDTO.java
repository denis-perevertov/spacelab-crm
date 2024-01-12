package com.example.spacelab.dto.course;

import com.example.spacelab.model.course.CourseStatus;
import lombok.Data;

@Data
public class CourseInfoPageDTO {

    private String name;
    private String icon;
    private CourseStatus status;
    private CourseInfoDTO info;
    private CourseMembersDTO members;
    private CourseTaskStructureDTO structure;

}
