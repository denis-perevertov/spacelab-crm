package com.example.spacelab.model.dto.student;

import com.example.spacelab.model.dto.CourseDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StudentDTO {

    private Long id;
    private String fullName;
    private String firstName, lastName, fathersName;
    private CourseDTO course;  // ?
    private String email;
    private String phone;
    private String telegram;
    private Integer rating;
    private String status;
    private String avatar;

}
