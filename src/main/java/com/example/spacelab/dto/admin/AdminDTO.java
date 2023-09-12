package com.example.spacelab.dto.admin;


import com.example.spacelab.dto.course.CourseListDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AdminDTO {

    private Long id;
    @Schema(example = "Test")
    private String fullName;
    @Schema(example = "Test")
    private String firstName, lastName;
    @Schema(example = "+380123456789")
    private String phone;
    @Schema(example = "test@gmail.com")
    private String email;
    @Schema(description = "Name of the role", example = "RoleName")
    private String role;
    private List<CourseListDTO> courses = new ArrayList<>();

}
