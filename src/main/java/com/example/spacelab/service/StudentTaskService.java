package com.example.spacelab.service;

import com.example.spacelab.model.dto.StudentTaskDTO;
import com.example.spacelab.util.StudentTaskStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentTaskService {

    List<StudentTaskDTO> getStudentTasks(Long id);
    List<StudentTaskDTO> getStudentTasks(Long id, StudentTaskStatus status);
    List<StudentTaskDTO> getStudentTasks(Long id, StudentTaskStatus status, Pageable pageable);
    StudentTaskDTO getStudentTask(Long taskID);


}
