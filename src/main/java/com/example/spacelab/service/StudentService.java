package com.example.spacelab.service;

import com.example.spacelab.model.InviteStudentRequest;
import com.example.spacelab.model.Student;
import com.example.spacelab.model.dto.student.StudentCardDTO;
import com.example.spacelab.model.dto.student.StudentDTO;
import com.example.spacelab.model.dto.StudentTaskDTO;
import com.example.spacelab.model.dto.student.StudentRegisterDTO;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.util.StudentTaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService extends StudentTaskService,
                                        StudentCardService,
                                        EntityFilterService<Student> {

    List<StudentDTO> getStudents();
    Page<StudentDTO> getStudents(Pageable pageable);
    Page<StudentDTO> getStudents(FilterForm filters, Pageable pageable);

    StudentDTO getStudentById(Long id);
    StudentDTO createNewStudent(StudentDTO dto);
    StudentDTO registerStudent(StudentRegisterDTO dto);
    StudentDTO editStudent(StudentDTO student);

    void deleteStudentById(Long id);

    String createInviteStudentToken(InviteStudentRequest request);
}
