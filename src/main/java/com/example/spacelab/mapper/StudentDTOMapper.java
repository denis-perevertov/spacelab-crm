package com.example.spacelab.mapper;

import com.example.spacelab.model.Student;
import com.example.spacelab.model.dto.StudentDTO;
import org.springframework.stereotype.Component;

@Component
public class StudentDTOMapper {

    public StudentDTO fromStudentToDTO(Student student) {
        StudentDTO dto = new StudentDTO();

        dto.setFullName(String.join(" ", student.getFirst_name(), student.getFathers_name(), student.getLast_name()));
        dto.setEmail(student.getEmail());
        dto.setPhone(student.getPhone());
        dto.setTelegram(student.getTelegram());
        dto.setRating(student.getRating());

        return dto;
    }

    public Student fromDTOToStudent(StudentDTO studentDTO) {

        return new Student();
    }
}
