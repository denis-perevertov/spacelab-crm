package com.example.spacelab.service;

import com.example.spacelab.model.InviteStudentRequest;
import com.example.spacelab.model.Student;
import com.example.spacelab.model.dto.StudentDTO;
import com.example.spacelab.model.dto.StudentTaskDTO;
import com.example.spacelab.model.dto.TaskDTO;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.util.StudentTaskStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService extends EntityService<Student>{

    List<StudentDTO> getStudents();
    List<StudentDTO> getStudents(Pageable pageable);
    List<StudentDTO> getStudents(FilterForm filters, Pageable pageable);

    StudentDTO getStudentDTOById(Long id);
    StudentDTO createNewStudent(StudentDTO dto);
    StudentDTO editStudent(StudentDTO student);

    void deleteStudentById(Long id);

    List<StudentTaskDTO> getStudentTasks(Long id);
    List<StudentTaskDTO> getStudentTasks(Long id, StudentTaskStatus status);
    List<StudentTaskDTO> getStudentTasks(Long id, StudentTaskStatus status, Pageable pageable);
    StudentTaskDTO getStudentTask(Long taskID);

    String createInviteStudentToken(InviteStudentRequest request);
}
