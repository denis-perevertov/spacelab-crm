package com.example.spacelab.model.dto;

import lombok.Data;

@Data
public class StudentDTO {

    private String fullName;
    private CourseDTO courseDTO;
    private String email;
    private String phone;
    private String telegram;
    private Integer rating;
    private String status;

}
