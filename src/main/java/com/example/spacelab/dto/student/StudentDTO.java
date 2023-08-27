package com.example.spacelab.dto.student;

import com.example.spacelab.dto.course.CourseListDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Map;

@Data
public class StudentDTO {

    private Long id;

    private String fullName;

    @NotBlank
    private String firstName, lastName, fathersName;

//    private CourseListDTO course;

    @NotBlank
    @Email
    private Map<String, Object> course;  // ?
    private String email;

    @NotBlank
    private String phone;

    private String telegram;

    private Integer rating;

    private String status;

    private String avatar;

}
