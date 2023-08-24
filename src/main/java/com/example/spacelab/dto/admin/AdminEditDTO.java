package com.example.spacelab.dto.admin;

import lombok.Data;

@Data
public class AdminEditDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;
    private String confirmPassword;

    private Long roleID;
    private Long courseID;

}
