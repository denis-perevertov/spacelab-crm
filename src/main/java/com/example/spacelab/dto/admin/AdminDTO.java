package com.example.spacelab.dto.admin;

import com.example.spacelab.dto.CourseDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AdminDTO {

    private Long id;
    private String fullName;
    private String firstName, lastName;
    private String phone;
    private String email;
    private String role;
    private List<CourseDTO> courses = new ArrayList<>();

}
