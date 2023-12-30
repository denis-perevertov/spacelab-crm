package com.example.spacelab.dto.course;

import lombok.Data;

@Data
public class CourseInfoPageDTO {

    private String name;
    private String icon;
    private CourseInfoDTO info;
    private CourseMembersDTO members;
    private CourseTaskStructureDTO structure;

}
