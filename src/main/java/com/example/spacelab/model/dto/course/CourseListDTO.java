package com.example.spacelab.model.dto.course;

import com.example.spacelab.util.CourseStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CourseListDTO {

    private Long id;

    private String name;

    private Long studentsQuantity;

    private LocalDate begin_date;

    private LocalDate end_date;

    private Long mentorId;
    private String mentorName;

    private Long managerId;
    private String managerName;

    private CourseStatus status;
}
