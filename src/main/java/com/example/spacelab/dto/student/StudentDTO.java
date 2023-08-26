package com.example.spacelab.dto.student;

import com.example.spacelab.dto.CourseDTO;
import lombok.Data;

import java.util.Map;

@Data
public class StudentDTO {

    private Long id;
    private String fullName;
    private String firstName, lastName, fathersName;
    private Map<String, Object> course;  // ?
    private String email;
    private String phone;
    private String telegram;
    private Integer rating;
    private String status;
    private String avatar;

}
