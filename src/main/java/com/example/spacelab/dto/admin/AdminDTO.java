package com.example.spacelab.dto.admin;


import com.example.spacelab.dto.course.CourseListDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
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

    private String avatar;
    private List<CourseListDTO> courses = new ArrayList<>();

    public AdminDTO(Long id, String fullName, String firstName, String lastName, String phone, String email, String role, String avatar, List<CourseListDTO> courses) {
        this.id = id;
        this.fullName = fullName.trim();
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.phone = phone.trim();
        this.email = email.trim();
        this.role = role.trim();
        this.avatar = avatar.trim();
        this.courses = courses;
    }
}
