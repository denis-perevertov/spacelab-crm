package com.example.spacelab.dto.course;

import com.example.spacelab.dto.student.StudentAvatarDTO;
import com.example.spacelab.model.course.CourseStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
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

    public CourseCardDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

//    private Map<Long, String> manegers;
//
//    private Map<Long, String> mentors;

}
