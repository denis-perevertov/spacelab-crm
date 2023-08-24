package com.example.spacelab.model.dto.student;

public record StudentEditDTO(Long id,
                             String firstName,
                             String lastName,
                             String fathersName,
                             Long courseID,
                             String email,
                             String phone,
                             String telegram) {

    public StudentEditDTO(Long id, StudentEditDTO dto) {
        this(id, dto.firstName(), dto.lastName(),
                dto.fathersName(), dto.courseID(),
                dto.email(), dto.phone(), dto.telegram());
    }

}
