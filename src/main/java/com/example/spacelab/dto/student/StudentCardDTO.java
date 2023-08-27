package com.example.spacelab.dto.student;

import com.example.spacelab.model.student.StudentDetails;
import lombok.Data;

@Data
public class StudentCardDTO {

    private StudentDetails studentDetails;

    private String roleName;
    private String courseName;


}
