package com.example.spacelab.service;

import com.example.spacelab.dto.task.TaskSubtaskListDTO;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.student.StudentTask;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.util.FilterForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface TaskService extends StudentTaskService, EntityFilterService<Task>{

    List<Task> getTasks();
    Page<Task> getTasks(Pageable pageable);
    Page<Task> getTasks(FilterForm filters, Pageable pageable);

    List<Task> getTasksByAllowedCourses(Long... ids);
    Page<Task> getTasksByAllowedCourses(Pageable pageable, Long... ids);
    Page<Task> getTasksByAllowedCourses(FilterForm filters, Pageable pageable, Long... ids);

    Task getTaskById(Long id);
    Task createNewTask(Task task);
    Task editTask(Task task);
    void deleteTaskById(Long id);

    List<Student> getTaskStudents(Long taskId);

    List<StudentTask> createStudentTaskListFromCourse(Course course);

    List<Task> getTaskSubtasks(Long id);

    Page<Task> getTasksWithoutCourse(Specification<Task> spec, Pageable pageable);
    Page<Task> getTasksWithoutCourse(Pageable pageable);
    Page<Task> getParentTasks(Specification<Task> spec, Pageable pageable);
    Page<Task> getParentTasks(Pageable pageable);

    List<Task> addSubtasksToTask(Long taskId, TaskSubtaskListDTO dto);
    void removeSubtaskFromList(Long taskId, Long subtaskId);

}
