package com.example.spacelab.dto.course;

import com.example.spacelab.dto.student.StudentAvatarDTO;
import com.example.spacelab.model.course.CourseStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class CourseCardDTO {

    private Long id;

    private String name;

    private LocalDate beginningDate;

    private LocalDate endDate;

    private Long mentorId;
    private String mentorName;

    private Long managerId;
    private String managerName;

    private List<StudentAvatarDTO> students;

    private Map<Long, String> tasks;

    private CourseStatus status;

    private CourseInformationDTO courseInfo;


//    private Map<Long, String> manegers;
//
//    private Map<Long, String> mentors;

}
