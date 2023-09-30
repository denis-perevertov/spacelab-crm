package com.example.spacelab.dto.student;

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

    public StudentEditDTO(Long id, StudentEditDTO dto) {
        this(id, dto.firstName(), dto.lastName(),
                dto.fathersName(), dto.courseID(),
                dto.email(), dto.phone(), dto.telegram(),
                dto.birthdate(), dto.status());
    }

}
