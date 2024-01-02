package com.example.spacelab.dto.student;

import com.example.spacelab.model.student.StudentDetails;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class StudentCardDTO {

    private StudentDetails studentDetails;

    @Schema(example = "RoleName")
    private String roleName;

    private Long courseId;

    @Schema(example = "CourseName")
    private String courseName;

    private String courseIcon;

    private String avatar;

}
