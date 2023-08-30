package com.example.spacelab.dto.course;

import com.example.spacelab.model.course.CourseStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CourseSaveCreatedDTO {

    private Long id;

    private String name;

    private LocalDate beginningDate;

    private Long mentorId;

    private Long managerId;




}
