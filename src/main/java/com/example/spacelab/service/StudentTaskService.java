package com.example.spacelab.service;

import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.student.StudentTask;
import com.example.spacelab.model.student.StudentTaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface StudentTaskService {

    List<StudentTask> getStudentTasks(Long id);
    List<StudentTask> getStudentTasks(Long id, StudentTaskStatus status);
    Page<StudentTask> getStudentTasks(Long id, StudentTaskStatus status, Pageable pageable);
    Page<StudentTask> getStudentTasks(Specification<StudentTask> spec, Pageable pageable);
    StudentTask getStudentTask(Long taskID);

    void createStudentTasksOnCourseTransfer(Student student, Course course);

    void completeStudentTask(Long taskID);


}
