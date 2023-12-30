package com.example.spacelab.dto.course;

import com.example.spacelab.dto.admin.AdminAvatarDTO;
import com.example.spacelab.dto.student.StudentAvatarDTO;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CourseEditDTO {

    private Long id;
    private String name;
    private String icon;
    private CourseInfoDTO info;
    private CourseMembersDTO members;
    private CourseTaskStructureDTO structure;
}
