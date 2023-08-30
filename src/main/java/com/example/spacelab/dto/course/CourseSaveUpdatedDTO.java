package com.example.spacelab.dto.course;

import com.example.spacelab.model.course.CourseStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CourseSaveUpdatedDTO {

    private Long id;

    private String name;

    private LocalDate beginningDate;

    private LocalDate endDate;

    private Long mentorId;

    private Long managerId;

    private List<Long> students;

    private List<Long> tasks;

    private List<Long> literature;

    private CourseStatus status;

    private CourseInformationDTO courseInfo;

}
