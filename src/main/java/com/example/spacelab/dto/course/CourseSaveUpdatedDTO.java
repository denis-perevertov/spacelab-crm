package com.example.spacelab.dto.course;

import com.example.spacelab.model.course.CourseStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class CourseSaveUpdatedDTO {

    private Long id;

    private String name;

    private LocalDate beginDate;

    private LocalDate endDate;

    private Long mentorID;

    private Long managerID;

    private List<Long> students;

    private List<Long> tasks;

    private List<Long> literature;

    private CourseStatus status;

    private CourseInformationDTO courseInfo;

    public CourseSaveUpdatedDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
