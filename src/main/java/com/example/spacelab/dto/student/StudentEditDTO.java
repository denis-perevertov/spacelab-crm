package com.example.spacelab.dto.student;

import com.example.spacelab.util.StringUtils;

import java.time.LocalDate;

public record StudentEditDTO(Long id,
                             String firstName,
                             String lastName,
                             String fathersName,
                             Long courseID,
                             String email,
                             String phone,
                             String telegram,
                             LocalDate birthdate,
                             String status) {

    public StudentEditDTO {
        firstName = StringUtils.trimString(firstName);
        lastName = StringUtils.trimString(lastName);
        fathersName = StringUtils.trimString(fathersName);
        email = StringUtils.trimString(email);
        phone = StringUtils.trimString(phone);
        telegram = StringUtils.trimString(telegram);
    }

    public StudentEditDTO(Long id, StudentEditDTO dto) {
        this(id, dto.firstName(), dto.lastName(),
                dto.fathersName(), dto.courseID(),
                dto.email(), dto.phone(), dto.telegram(),
                dto.birthdate(), dto.status());
    }

}
