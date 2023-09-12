package com.example.spacelab.dto.course;

import com.example.spacelab.dto.student.StudentAvatarDTO;
import com.example.spacelab.model.course.CourseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class CourseCardDTO {

    @Schema(example = "4")
    private Long id;

    @Schema(example = "CourseName")
    private String name;

    private LocalDate beginningDate;

    private LocalDate endDate;

    @Schema(example = "3")
    private Long mentorId;
    @Schema(example = "MentorName")
    private String mentorName;

    @Schema(example = "4")
    private Long managerId;
    @Schema(example = "ManagerName")
    private String managerName;

    private List<StudentAvatarDTO> students;

    private Map<Long, String> tasks;

    @Schema(example = "ACTIVE")
    private CourseStatus status;

    private CourseInformationDTO courseInfo;

    public CourseCardDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

//    private Map<Long, String> manegers;
//
//    private Map<Long, String> mentors;

}
