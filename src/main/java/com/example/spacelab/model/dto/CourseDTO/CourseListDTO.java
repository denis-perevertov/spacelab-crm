package com.example.spacelab.model.dto.CourseDTO;

import com.example.spacelab.model.dto.AdminDTO;
import com.example.spacelab.model.dto.StudentDTO;
import com.example.spacelab.util.CourseStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

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
