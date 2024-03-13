package com.example.spacelab.dto.course;

import com.example.spacelab.model.course.CourseStatus;
import com.example.spacelab.util.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CourseEditDTO {

    private Long id;
    private String name;
    private String icon;
    private CourseStatus status;
    private CourseInfoDTO info;
    private CourseMembersDTO members;
    private CourseTaskStructureDTO structure;

    public CourseEditDTO(Long id, String name, String icon, CourseStatus status, CourseInfoDTO info, CourseMembersDTO members, CourseTaskStructureDTO structure) {
        this.id = id;
        this.name = StringUtils.trimString(name);
        this.icon = StringUtils.trimString(icon);
        this.status = status;
        this.info = info;
        this.members = members;
        this.structure = structure;
    }
}
