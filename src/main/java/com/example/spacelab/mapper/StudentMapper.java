package com.example.spacelab.mapper;

import com.example.spacelab.exception.MappingException;
import com.example.spacelab.model.Student;
import com.example.spacelab.model.StudentDetails;
import com.example.spacelab.model.dto.student.StudentCardDTO;
import com.example.spacelab.model.dto.student.StudentDTO;
import com.example.spacelab.model.dto.student.StudentRegisterDTO;
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

//            if(student.getAccountStatus() != null)
//                dto.setStatus(student.getAccountStatus().toString());

            if(student.getCourse() != null) {
                dto.setCourse(courseMapper.fromCourseToDTO(student.getCourse()));
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
            dto.setRole(student.getRole().getName());
            dto.setCourseName(student.getCourse().getName());
//            dto.setStatus(student.getAccountStatus());
//            dto.setBirthdate(student.getBirthdate());

//            dto.setEducationLevel(student.getEducationLevel());
//            dto.setEnglishLevel(student.getEnglishLevel());
//            dto.setWorkStatus(student.getWorkStatus());

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
