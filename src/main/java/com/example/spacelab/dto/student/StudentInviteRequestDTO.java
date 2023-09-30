package com.example.spacelab.dto.student;

import lombok.Data;

@Data
public class StudentInviteRequestDTO {

    private String firstName;
    private String lastName;
    private String fathersName;
    private String email;
    private String phone;
    private Long courseID;

}
