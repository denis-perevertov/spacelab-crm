package com.example.spacelab.service;

import com.example.spacelab.dto.student.StudentTaskLessonDTO;
import com.example.spacelab.dto.task.StudentTaskPointDTO;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.student.StudentTask;
import com.example.spacelab.model.student.StudentTaskStatus;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.util.FilterForm;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
@Hidden
public interface StudentTaskService {

    List<StudentTask> getStudentTasks(Long id);
    List<StudentTask> getStudentTasks(Long id, StudentTaskStatus status);
    Page<StudentTask> getStudentTasks(Long id, StudentTaskStatus status, Pageable pageable);
    Page<StudentTask> getStudentTasks(Specification<StudentTask> spec, Pageable pageable);
    StudentTask getStudentTask(Long taskID);
    StudentTask fromTaskToStudentTask(Task task);

    List<StudentTaskLessonDTO> getOpenStudentTasks(Student student);
    List<StudentTaskLessonDTO> getNextStudentTasks(Student student);

    List<StudentTaskPointDTO> getStudentTaskProgressPoints(Long taskId);

    /*
    todo
    - delete old student tasks after task transfer to different course ?
     */

    void createStudentTasksOnStudentCourseTransfer(Student student, Course course);
    void clearStudentTasksOnStudentDeletionFromCourse(Student student);

    void lockStudentTask(Long taskID);
    void unlockStudentTask(Long taskID);
    void completeStudentTask(Long taskID);
    void resetStudentTask(Long taskID);

    Specification<StudentTask> buildSpec(FilterForm filters);

}
