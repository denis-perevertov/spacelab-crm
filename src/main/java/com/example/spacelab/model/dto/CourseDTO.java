package com.example.spacelab.model.dto;

import com.example.spacelab.model.dto.admin.AdminDTO;
import com.example.spacelab.model.dto.student.StudentDTO;
import com.example.spacelab.util.CourseStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CourseDTO {

    private Long id;
    private String name;
    private List<StudentDTO> students;
    private LocalDate begin_date;
    private LocalDate end_date;
    private AdminDTO mentor;
    private AdminDTO manager;
    private CourseStatus status;
}
