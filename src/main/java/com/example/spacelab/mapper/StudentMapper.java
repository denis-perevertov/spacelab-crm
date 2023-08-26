package com.example.spacelab.mapper;

import com.example.spacelab.exception.MappingException;
import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.student.StudentDetails;
import com.example.spacelab.dto.student.StudentCardDTO;
import com.example.spacelab.dto.student.StudentDTO;
import com.example.spacelab.dto.student.StudentEditDTO;
import com.example.spacelab.dto.student.StudentRegisterDTO;
import com.example.spacelab.repository.CourseRepository;
import com.example.spacelab.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Log
@RequiredArgsConstructor
public class StudentMapper {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public StudentDTO fromStudentToDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        StudentDetails studentDetails = student.getDetails();

        try {
            dto.setId(student.getId());

            dto.setFullName(String.join(" ", studentDetails.getFirstName(),
                    studentDetails.getFathersName(), studentDetails.getLastName()));
            dto.setFirstName(studentDetails.getFirstName());
            dto.setFathersName(studentDetails.getFathersName());
            dto.setLastName(studentDetails.getLastName());
            dto.setEmail(studentDetails.getEmail());
            dto.setPhone(studentDetails.getPhone());
            dto.setTelegram(studentDetails.getTelegram());
            if(studentDetails.getAccountStatus() != null)
                dto.setStatus(studentDetails.getAccountStatus().toString());

            dto.setRating(student.getRating());

            dto.setAvatar(student.getAvatar());

            if(student.getCourse() != null) {
                dto.setCourse(Map.of("id", student.getCourse().getId(), "name", student.getCourse().getName()));
            }
        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("DTO: " + dto);
            throw new MappingException(e.getMessage());

        }

        return dto;
    }

    public StudentCardDTO fromStudentToCardDTO(Student student) {
        StudentCardDTO dto = new StudentCardDTO();

        try {

            dto.setStudentDetails(student.getDetails());

            if(student.getRole() != null)
                dto.setRoleName(student.getRole().getName());
            if(student.getCourse() != null)
                dto.setCourseName(student.getCourse().getName());

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
            StudentDetails studentDetails = student.getDetails();
            studentDetails.setFirstName(studentDTO.getFirstName());
            studentDetails.setFathersName(studentDTO.getFathersName());
            studentDetails.setLastName(studentDTO.getLastName());
            studentDetails.setEmail(studentDTO.getEmail());
            studentDetails.setPhone(studentDTO.getPhone());
            studentDetails.setTelegram(studentDTO.getTelegram());

            student.setRating(studentDTO.getRating());
            student.setAvatar(studentDTO.getAvatar());

            if(studentDTO.getCourse() != null) {
                student.setCourse(courseRepository.getReferenceById((Long) studentDTO.getCourse().get("id")));
            }
        } catch (EntityNotFoundException e) {
            log.severe("Error: " + e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("Entity: " + student);
            throw new MappingException(e.getMessage());

        }

        return student;
    }

    public Student fromEditDTOToStudent(StudentEditDTO dto) {
        Student student = (dto.id() != null) ?
                studentRepository.getReferenceById(dto.id()) :
                new Student();

        try {
            StudentDetails studentDetails = student.getDetails();
            studentDetails.setFirstName(dto.firstName());
            studentDetails.setFathersName(dto.fathersName());
            studentDetails.setLastName(dto.lastName());
            studentDetails.setEmail(dto.email());
            studentDetails.setPhone(dto.phone());
            studentDetails.setTelegram(dto.telegram());

            if(dto.courseID() != null && dto.courseID() != 0)
                student.setCourse(courseRepository.getReferenceById(dto.courseID()));

        } catch (EntityNotFoundException e) {
            log.severe("Error: " + e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("Entity: " + student);
            throw new MappingException(e.getMessage());
        }

        return student;
    }

    public Student fromRegisterDTOToStudent(StudentRegisterDTO dto) {
        Student student = new Student();

        try {
            StudentDetails details = student.getDetails();
            details.setFirstName(dto.getFirstName());
            details.setLastName(dto.getLastName());
            details.setFathersName(dto.getFathersName());
            details.setBirthdate(dto.getBirthdate());

            details.setPhone(dto.getPhone());
            details.setEmail(dto.getEmail());
            details.setTelegram(dto.getTelegram());
            details.setAddress(dto.getAddress());
            details.setGithubLink(dto.getGithubLink());
            details.setLinkedinLink(dto.getLinkedinLink());

            details.setEducationLevel(dto.getEducationLevel());
            details.setEnglishLevel(dto.getEnglishLevel());
            details.setWorkStatus(dto.getWorkStatus());

            student.setAvatar(dto.getAvatar());

        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("Entity: " + student);
            throw new MappingException(e.getMessage());

        }

        return student;
    }
}
