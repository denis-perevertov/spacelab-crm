package com.example.spacelab.model.dto.course;

import com.example.spacelab.util.CourseStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class CourseCardDTO {

    private Long id;

    private String name;

    private LocalDate beginningDate;

    private LocalDate endDate;

    private Long mentorId;
    private String mentorName;

    private Long managerId;
    private String managerName;

    private Map<Long, String> students;

    private Map<Long, String> tasks;

    private CourseStatus status;

    private String main_description;

    private List<String> topics;

    private String completionTime;

    private Integer groupSize;

    private Integer hoursNorm;

    private Map<Long, String> manegers;

    private Map<Long, String> mentors;

}
