package com.example.spacelab.model.dto;

import com.example.spacelab.util.CourseStatus;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
public class CourseDTO {

    private Long id;
    private List<StudentDTO> students;
    private LocalDate begin_date;
    private LocalDate end_date;
    private AdminDTO mentor;
    private AdminDTO manager;
    private CourseStatus status;
}
