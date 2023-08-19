package com.example.spacelab.model.dto.student;

import com.example.spacelab.model.StudentDetails;
import com.example.spacelab.util.StudentAccountStatus;
import com.example.spacelab.util.StudentEducationLevel;
import com.example.spacelab.util.StudentEnglishLevel;
import com.example.spacelab.util.StudentWorkStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentCardDTO {

    private StudentDetails studentDetails;

    private String role;
    private String courseName;

    private StudentAccountStatus status;

    private LocalDate birthdate;

    private StudentEducationLevel educationLevel;
    private StudentEnglishLevel englishLevel;
    private StudentWorkStatus workStatus;

}
