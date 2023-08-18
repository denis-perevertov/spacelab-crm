package com.example.spacelab.service;

import com.example.spacelab.model.dto.StudentTaskDTO;
import com.example.spacelab.util.StudentTaskStatus;

import java.util.List;

public interface StudentTaskService {

    List<StudentTaskDTO> getStudentTasks(Long id);
    List<StudentTaskDTO> getStudentTasks(Long id, StudentTaskStatus status);
    StudentTaskDTO getStudentTask(Long taskID);


}
