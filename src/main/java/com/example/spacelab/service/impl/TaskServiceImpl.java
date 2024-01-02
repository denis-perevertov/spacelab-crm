package com.example.spacelab.service.impl;

import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.mapper.TaskMapper;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.student.StudentAccountStatus;
import com.example.spacelab.model.student.StudentTask;
import com.example.spacelab.model.student.StudentTaskStatus;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.model.task.TaskLevel;
import com.example.spacelab.model.task.TaskStatus;
import com.example.spacelab.repository.CourseRepository;
import com.example.spacelab.repository.StudentRepository;
import com.example.spacelab.repository.StudentTaskRepository;
import com.example.spacelab.repository.TaskRepository;
import com.example.spacelab.service.TaskService;
import com.example.spacelab.service.specification.StudentSpecifications;
import com.example.spacelab.service.specification.TaskSpecifications;
import com.example.spacelab.util.FilterForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final StudentTaskRepository studentTaskRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Override
    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    @Override
    public List<Student> getTaskStudents(Long taskId) {
        return getTaskById(taskId).getActiveStudents();
    }

    @Override
    public Page<Task> getTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    @Override
    public Page<Task> getTasks(FilterForm filters, Pageable pageable) {
        Specification<Task> spec = buildSpecificationFromFilters(filters);
        return taskRepository.findAll(spec, pageable);
    }

    @Override
    public List<Task> getTasksByAllowedCourses(Long... ids) {
        return taskRepository.findAllByAllowedCourse(ids);
    }

    @Override
    public Page<Task> getTasksByAllowedCourses(Pageable pageable, Long... ids) {
        return taskRepository.findAllByAllowedCoursePage(pageable, ids);
    }

    @Override
    public Page<Task> getTasksByAllowedCourses(FilterForm filters, Pageable pageable, Long... ids) {
        Specification<Task> spec = buildSpecificationFromFilters(filters).and(TaskSpecifications.hasCourseIDs(ids));
        return taskRepository.findAll(spec, pageable);
    }

    @Override
    public Task getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        return task;
    }

    @Override
    public Task createNewTask(Task taskIn) {
        Task task = taskRepository.save(taskIn);
        return task;
    }

    @Override
    public Task editTask(Task taskIn) {
        Task task = taskRepository.save(taskIn);
        return task;
    }

    @Override
    @Transactional
    public void deleteTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow();
        taskRepository.findTasksByParentTask(task).forEach(subtask -> {
            subtask.setParentTask(null);
            subtask.setStatus(TaskStatus.INACTIVE);
            taskRepository.save(subtask);
        });
        taskRepository.deleteById(id);
    }

    @Override
    public StudentTask unlockTaskForStudent(Long taskID, Long studentID) {
        Student student = studentRepository.findById(studentID).orElseThrow();
        Task originalTask = getTaskById(taskID);
        log.info("Unlocking Task (ID: {}) for Student (ID: {})", taskID, studentID);
        StudentTask studentTask = new StudentTask();
        studentTask.setStudent(student);
        studentTask.setTaskReference(originalTask);
        studentTask.setSubtasks(new ArrayList<>());
        studentTask.setBeginDate(LocalDate.now());
        studentTask.setStatus(StudentTaskStatus.UNLOCKED);
        studentTask.setPercentOfCompletion(0);
        studentTask = studentTaskRepository.save(studentTask);
        originalTask.getActiveStudents().add(student);
        taskRepository.save(originalTask);
        log.info("Created Student Copy of Task (ID: {}): {}", taskID, studentTask);
        return studentTask;
    }



    @Override
    public List<Task> getTaskSubtasks(Long id) {
        return taskRepository.findTaskSubtasks(id);
    }

    @Override
    public Page<Task> getAvailableTasks(Pageable pageable) {
        Page<Task> page = taskRepository.findAvailableParentTasks(pageable);
        log.info("found available tasks: {}", page);
        return page;
    }

    @Override
    public List<StudentTask> getStudentTasks(Long studentID) {
        log.info("Getting tasks of student w/ ID: " + studentID);
        return studentTaskRepository.findStudentTasks(studentID);
    }

    @Override
    public List<StudentTask> getStudentTasks(Long studentID, StudentTaskStatus status) {
        log.info("Getting tasks(STATUS:"+status.toString()+") of student w/ ID: " + studentID);
        return studentTaskRepository.findStudentTasksWithStatus(studentID, status);
    }

    @Override
    public Page<StudentTask> getStudentTasks(Long studentID, StudentTaskStatus status, Pageable pageable) {
        log.info("Getting "+pageable.getPageSize()+" tasks(STATUS:"+status.toString()+")" +
                " of student w/ ID: " + studentID +
                " || page " + pageable.getPageNumber());
        return studentTaskRepository.findStudentTasksWithStatusAndPage(studentID, status, pageable);
    }

    @Override
    public Page<StudentTask> getStudentTasks(Specification<StudentTask> spec, Pageable pageable) {
        return studentTaskRepository.findAll(spec, pageable);
    }

    @Override
    public StudentTask getStudentTask(Long taskID) {
        log.info("Getting student task with taskID: " + taskID);
        StudentTask task = studentTaskRepository.findById(taskID).orElseThrow(() -> new ResourceNotFoundException("Student task not found", StudentTask.class));
        return task;
    }

    @Override
    public void createStudentTasksOnCourseTransfer(Student student, Course course) {
        log.info("creating student tasks at the moment of transfering student(id:{}) to course(id:{})", student.getId(), course.getId());
        // clear student tasks which were not completed
        List<StudentTask> oldStudentTasks = student.getTasks();
        oldStudentTasks.stream()
                .filter(studentTask -> studentTask.getStatus() != StudentTaskStatus.COMPLETED)
                .forEach(studentTaskRepository::delete);

        List<Task> taskReferences = oldStudentTasks.stream().map(StudentTask::getTaskReference).toList();

        // create new task snapshots for student (not saved in db yet)
        List<StudentTask> newStudentTasks = createStudentTaskListFromCourse(course);
        newStudentTasks.forEach(newTask -> {
            if(!taskReferences.contains(newTask.getTaskReference())) {
                // save any new student task to db
                newTask.setStudent(student);
                newTask = studentTaskRepository.save(newTask);
                oldStudentTasks.add(newTask);
            }
        });

    }

    @Override
    public void clearStudentTasksOnDeletionFromCourse(Student student) {
        student.getTasks().stream().filter(task -> task.getStatus() != StudentTaskStatus.COMPLETED)
                .forEach(studentTaskRepository::delete);
    }



    @Override
    public List<StudentTask> createStudentTaskListFromCourse(Course course) {
        return course.getTasks().stream().map(this::fromTaskToStudentTask).toList();
    }

    @Override
    public StudentTask fromTaskToStudentTask(Task task) {
        // base case to exit recursion
        if(task == null) return null;

        StudentTask st = new StudentTask();
        st.setTaskReference(task);
//        st.setParentTask(fromTaskToStudentTask(task.getParentTask()));
        st.setPercentOfCompletion(0);
        st.setStatus(StudentTaskStatus.LOCKED);
        task.getSubtasks().forEach(subtask -> Optional.ofNullable(fromTaskToStudentTask(subtask)).ifPresent(studentSubtask -> st.getSubtasks().add(studentSubtask)));

        return st;
    }

    // todo check statuses
    @Override
    public void lockStudentTask(Long taskID) {
        StudentTask task = studentTaskRepository.findById(taskID).orElseThrow();
        task.getTaskReference().getActiveStudents().remove(task.getStudent());
        task.setStatus(StudentTaskStatus.LOCKED);
        task.setBeginDate(null);
        task.setEndDate(null);
        studentTaskRepository.save(task);
        log.info("task locked");
    }

    // todo check statuses
    @Override
    public void unlockStudentTask(Long taskID) {
        StudentTask task = studentTaskRepository.findById(taskID).orElseThrow();
        task.getTaskReference().getActiveStudents().add(task.getStudent());
        task.setStatus(StudentTaskStatus.UNLOCKED);
        task.setBeginDate(LocalDate.now());
        task.setEndDate(null);
        studentTaskRepository.save(task);
        log.info("task unlocked");
    }

    // todo check statuses
    @Override
    public void completeStudentTask(Long taskID) {
        StudentTask task = studentTaskRepository.findById(taskID).orElseThrow();
        task.getTaskReference().getActiveStudents().remove(task.getStudent());
        task.setStatus(StudentTaskStatus.COMPLETED);
        task.setEndDate(LocalDate.now());
        studentTaskRepository.save(task);
        log.info("task completed");
    }

    // todo check statuses
    @Override
    public void resetStudentTask(Long taskID) {
        StudentTask task = studentTaskRepository.findById(taskID).orElseThrow();
        task.getTaskReference().getActiveStudents().add(task.getStudent());
        task.setStatus(StudentTaskStatus.UNLOCKED);
        task.setBeginDate(LocalDate.now());
        task.setEndDate(null);
        studentTaskRepository.save(task);
        log.info("task reset");
    }


    @Override
    public Specification<Task> buildSpecificationFromFilters(FilterForm filters) {
        log.info("Building specification from filters: " + filters);

        String name = filters.getName();
        Long courseID = filters.getCourse();
        String levelInput = filters.getLevel();
        String statusInput = filters.getStatus();

        Course course = (courseID == null) ? null : courseRepository.getReferenceById(courseID);
        TaskStatus status = (statusInput == null) ? null : TaskStatus.valueOf(statusInput);
        TaskLevel level = (levelInput == null) ? null : TaskLevel.valueOf(levelInput);

        Specification<Task> spec = Specification.where(TaskSpecifications.hasNameLike(name))
                .and(TaskSpecifications.hasCourse(course))
                .and(TaskSpecifications.hasLevel(level))
                .and(TaskSpecifications.hasStatus(status));

        return spec;
    }
}
