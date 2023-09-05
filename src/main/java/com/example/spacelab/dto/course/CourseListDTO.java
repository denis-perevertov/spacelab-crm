package com.example.spacelab.dto.course;

import com.example.spacelab.model.course.CourseStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
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

    public CourseListDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
