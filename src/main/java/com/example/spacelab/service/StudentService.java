package com.example.spacelab.service;

import com.example.spacelab.model.Student;
import com.example.spacelab.model.dto.StudentDTO;
import com.example.spacelab.model.dto.TaskDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService {
    List<StudentDTO> getStudents();
    List<StudentDTO> getStudentsByPage(Pageable pageable);
    Student getStudentById(Long id);
    StudentDTO getStudentDTOById(Long id);
    StudentDTO createNewStudent(StudentDTO dto);
    StudentDTO editStudent(StudentDTO student);
    void deleteStudentById(Long id);

    List<TaskDTO> getStudentTasks(Long id);
}
