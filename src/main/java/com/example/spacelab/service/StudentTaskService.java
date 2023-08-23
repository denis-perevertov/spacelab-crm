package com.example.spacelab.service;

import com.example.spacelab.model.StudentTask;
import com.example.spacelab.model.dto.StudentTaskDTO;
import com.example.spacelab.util.StudentTaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentTaskService {

    List<StudentTask> getStudentTasks(Long id);
    List<StudentTask> getStudentTasks(Long id, StudentTaskStatus status);
    Page<StudentTask> getStudentTasks(Long id, StudentTaskStatus status, Pageable pageable);
    StudentTask getStudentTask(Long taskID);


}
