package com.example.spacelab.model.dto.CourseDTO;

import com.example.spacelab.util.CourseStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CourseSaveDTO {

    private Long id;

    private String name;

    private LocalDate beginningDate;

    private LocalDate endDate;

    private Long mentorId;

    private Long managerId;

    private List<Long> students;

    private List<Long> tasks;

    private CourseStatus status;

    private String main_description;

    private List<String> topics;

    private String completionTime;

    private Integer groupSize;

    private Integer hoursNorm;


}
