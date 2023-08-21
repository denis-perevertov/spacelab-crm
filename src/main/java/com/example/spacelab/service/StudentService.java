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

    List<Student> getStudents();
    Page<Student> getStudents(Pageable pageable);
    Page<Student> getStudents(FilterForm filters, Pageable pageable);

    Student getStudentById(Long id);
    Student createNewStudent(Student student);
    Student registerStudent(Student student);
    Student editStudent(Student student);

    void deleteStudentById(Long id);

    String createInviteStudentToken(InviteStudentRequest request);
}
