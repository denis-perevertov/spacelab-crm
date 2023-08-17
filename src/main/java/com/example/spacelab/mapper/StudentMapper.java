package com.example.spacelab.mapper;

import com.example.spacelab.model.Student;
import com.example.spacelab.model.dto.StudentDTO;
import com.example.spacelab.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentMapper {

    private final StudentRepository studentRepository;

    public StudentDTO fromStudentToDTO(Student student) {
        StudentDTO dto = new StudentDTO();

        dto.setId(student.getId());
        dto.setFullName(String.join(" ", student.getFirstName(), student.getFathersName(), student.getLastName()));
        dto.setEmail(student.getEmail());
        dto.setPhone(student.getPhone());
        dto.setTelegram(student.getTelegram());
        dto.setRating(student.getRating());
        // todo set status

        return dto;
    }

    public Student fromDTOToStudent(StudentDTO studentDTO) {
        Student student =
                (studentDTO.getId() != null ?
                studentRepository.getReferenceById(studentDTO.getId()) :
                new Student());

        student.setFirstName(studentDTO.getFullName().split(" ")[0]);
        student.setFathersName(studentDTO.getFullName().split(" ")[1]);
        student.setLastName(studentDTO.getFullName().split(" ")[2]);
        student.setEmail(studentDTO.getEmail());
        student.setPhone(studentDTO.getPhone());
        student.setTelegram(studentDTO.getTelegram());
        student.setRating(studentDTO.getRating());
        // todo set status

        return student;
    }
}
