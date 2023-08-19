package com.example.spacelab.mapper;

import com.example.spacelab.exception.MappingException;
import com.example.spacelab.model.Student;
import com.example.spacelab.model.dto.StudentDTO;
import com.example.spacelab.repository.CourseRepository;
import com.example.spacelab.repository.StudentRepository;
import com.example.spacelab.util.StudentAccountStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

@Component
@Log
@RequiredArgsConstructor
public class StudentMapper {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public StudentDTO fromStudentToDTO(Student student) {
        StudentDTO dto = new StudentDTO();

        try {
            dto.setFullName(String.join(" ", student.getFirstName(), student.getFathersName(), student.getLastName()));
            dto.setFirstName(student.getFirstName());
            dto.setFathersName(student.getFathersName());
            dto.setLastName(student.getLastName());
            dto.setEmail(student.getEmail());
            dto.setPhone(student.getPhone());
            dto.setTelegram(student.getTelegram());
            dto.setRating(student.getRating());
            dto.setStatus(student.getAccountStatus().toString());

            if(student.getCourse() != null) {
                dto.setCourse(courseMapper.fromCourseToListDTO(student.getCourse()));
            }
        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("DTO: " + dto);
            throw new MappingException(e.getMessage());

        }

        return dto;
    }

    public Student fromDTOToStudent(StudentDTO studentDTO) {
        Student student =
                (studentDTO.getId() != null ?
                studentRepository.getReferenceById(studentDTO.getId()) :
                new Student());

        try {
            student.setFirstName(studentDTO.getFirstName());
            student.setFathersName(studentDTO.getFathersName());
            student.setLastName(studentDTO.getLastName());
            student.setEmail(studentDTO.getEmail());
            student.setPhone(studentDTO.getPhone());
            student.setTelegram(studentDTO.getTelegram());
            student.setRating(studentDTO.getRating());
            student.setAccountStatus(StudentAccountStatus.valueOf(studentDTO.getStatus()));

            if(studentDTO.getCourse() != null) {
                student.setCourse(courseRepository.getReferenceById(studentDTO.getCourse().getId()));
            }
        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("Entity: " + student);
            throw new MappingException(e.getMessage());

        }

        return student;
    }
}
