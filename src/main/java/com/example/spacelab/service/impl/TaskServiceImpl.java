package com.example.spacelab.service.impl;

import com.example.spacelab.dto.student.StudentTaskLessonDTO;
import com.example.spacelab.dto.task.*;
import com.example.spacelab.exception.BlockedEntityException;
import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.exception.TeamworkException;
import com.example.spacelab.integration.TaskTrackingService;
import com.example.spacelab.integration.data.*;
import com.example.spacelab.mapper.TaskMapper;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.student.StudentTask;
import com.example.spacelab.model.student.StudentTaskStatus;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.model.task.TaskFile;
import com.example.spacelab.model.task.TaskLevel;
import com.example.spacelab.model.task.TaskStatus;
import com.example.spacelab.repository.CourseRepository;
import com.example.spacelab.repository.StudentRepository;
import com.example.spacelab.repository.StudentTaskRepository;
import com.example.spacelab.repository.TaskRepository;
import com.example.spacelab.service.FileService;
import com.example.spacelab.service.NotificationService;
import com.example.spacelab.service.PDFService;
import com.example.spacelab.service.TaskService;
import com.example.spacelab.service.specification.TaskSpecifications;
import com.example.spacelab.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.comparator.Comparators;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

import static com.example.spacelab.service.specification.StudentTaskSpecifications.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@Log4j2
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final StudentTaskRepository studentTaskRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final PDFService pdfService;

    private final TaskTrackingService trackingService;

    private final NotificationService notificationService;

    private final FileService fileService;
    private final TaskMapper mapper;

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
    @Transactional
    public Task createNewTask(Task taskIn) {
        Task task = taskRepository.save(taskIn);
        if(task.getCourse() != null) {
            // create student task for every student of the course
            createStudentTasksOnTaskCourseTransfer(task);
        }
        return task;
    }

    @Override
    @Transactional
    public Task createNewTask(TaskSaveDTO dto) {
        Task task = mapper.fromTaskSaveDTOToTask(dto);
        task = taskRepository.save(task);

        task.getTaskProgressPoints().clear();
        task.getTaskProgressPoints().addAll(
                new ArrayList<>(dto.getTaskProgressPoints().stream().map(mapper::fromDTOToPoint).toList())
        );

        if(task.getCourse() != null) {
            // create student task for every student of the course
            createStudentTasksOnTaskCourseTransfer(task);
            task.setTaskIndex(task.getCourse().getTasks().size());
        }
        List<TaskFileDTO> files = dto.getFiles();
        if(files != null && !files.isEmpty()) {
            for(TaskFileDTO file : files) {
                if(file.id() == null && file.file() != null && !file.file().isEmpty()) {
                    try {
                        String filename = FilenameUtils.generateFileName(file.file());
                        fileService.saveFile(file.file(), filename, "tasks");
                        task.getFiles().add(new TaskFile().setName(file.name()).setLink(filename));
                    } catch (Exception e) {
                        log.warn("could not save file: {}", file.file());
                        log.warn(e.getMessage());
                    }
                }
            }
        }
        task = taskRepository.save(task);
        if(task.getCourse() != null) {
            notificationService.sendTaskAddedToCourseNotification(task, task.getCourse());
        }
        return task;
    }

    @Override
    @Transactional
    public Task editTask(Task taskIn) {
        Task task = taskRepository.save(taskIn);
        // fixme CATCH TRANSFER correctly
        if(task.getCourse() != null) {
            createStudentTasksOnTaskCourseTransfer(task);
        }
        return task;
    }

    @Override
    @Transactional
    public Task editTask(TaskSaveDTO dto) {
        Task task = mapper.fromTaskSaveDTOToTask(dto);
        task = taskRepository.save(task);
                                                                // fixme CATCH TRANSFER correctly
        task.getTaskProgressPoints().clear();
        task.getTaskProgressPoints().addAll(
                new ArrayList<>(dto.getTaskProgressPoints().stream().map(mapper::fromDTOToPoint).toList())
        );

        if(task.getCourse() != null) {
            createStudentTasksOnTaskCourseTransfer(task);
        }

//        task.getFiles().clear();
//        taskRepository.save(task);

        List<TaskFileDTO> files = dto.getFiles();
        if(files != null && !files.isEmpty()) {
            for(TaskFileDTO file : files) {
                // 1. ID == null && file is not empty = download new file
                if(file.id() == null && file.file() != null && !file.file().isEmpty()) {
                    try {
                        String filename = FilenameUtils.generateFileName(file.file());
                        fileService.saveFile(file.file(), filename, "tasks");
                        task.getFiles().add(new TaskFile().setName(file.name()).setLink(filename));
                    } catch (Exception e) {
                        log.warn("could not save file: {}", file.file());
                        log.warn(e.getMessage());
                    }
                }
                // 2. ID != null && file is not empty = rewrite file
                else if(file.id() != null && file.file() != null && !file.file().isEmpty()) {
                    try {
                        String filename = FilenameUtils.generateFileName(file.file());
                        fileService.saveFile(file.file(), filename, "tasks");
                        task.getFiles().forEach(tf -> {
                            if(tf.getId().equals(file.id())) {
                                tf.setName(file.name()).setLink(file.link());
                            }
                        });
                    } catch (Exception e) {
                        log.warn("could not save file: {}", file.file());
                        log.warn(e.getMessage());
                    }
                }
                // 3. ID != null && file is empty = leave as is, no changes
            }
            // 4. remove file ?
            // get task file ids to remove
            List<Long> fileIds = files.stream().map(TaskFileDTO::id).filter(Objects::nonNull).toList();
            task.getFiles().removeIf(tf -> !fileIds.contains(tf.getId()));
        }

        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public void deleteTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow();
        List<Task> subtasks = taskRepository.findTasksByParentTask(task);
        subtasks.forEach(subtask -> {
            subtask.setParentTask(null);
            subtask.setStatus(TaskStatus.INACTIVE);
        });
        taskRepository.saveAll(subtasks);
        log.info("subtasks with parent task(id:{}) edited", id);
        studentTaskRepository.deleteStudentTasksByTaskReference(task);
        log.info("student tasks with task reference(id:{}) deleted", id);
        taskRepository.deleteById(id);
        log.info("task(id:{}) deleted", id);
    }

    public StudentTask unlockTaskForStudent(Long taskID, Long studentID) {
        Student student = studentRepository.findById(studentID).orElseThrow();
        Task originalTask = getTaskById(taskID);
        log.info("Unlocking Task (ID: {}) for Student (ID: {})", taskID, studentID);
        StudentTask studentTask = new StudentTask();
        studentTask.setStudent(student);
        studentTask.setTaskReference(originalTask);
        studentTask.setSubtasks(new ArrayList<>());
        studentTask.setBeginDate(ZonedDateTime.now());
        studentTask.setStatus(StudentTaskStatus.UNLOCKED);
        studentTask.setPercentOfCompletion(0);
        studentTask = studentTaskRepository.save(studentTask);
        originalTask.getActiveStudents().add(student);
        taskRepository.save(originalTask);
        log.info("Created Student Copy of Task (ID: {}): {}", taskID, studentTask);
        return studentTask;
    }

    private void createStudentTasksOnTaskCourseTransfer(Task task) {
        log.info("creating new student tasks on course transfer (task ref ID: {})", task.getId());
        task.getCourse().getStudents().forEach(st -> {
            st.getTasks()
                    .stream()
                    .map(t -> t.getTaskReference().getId())
                    .filter(id -> id.equals(task.getId()))
                    .findAny()
                    .ifPresentOrElse((id) -> {}, () -> {
                        StudentTask studentTask = new StudentTask()
                                .setStudent(st)
                                .setTaskReference(task);
                        studentTask = studentTaskRepository.save(studentTask);
                        st.getTasks().add(studentTask);
                    });
        });
    }

    @Override
    public List<Task> getTaskSubtasks(Long id) {
        return taskRepository.findTaskSubtasks(id)
                .stream()
                .sorted(Comparator.comparing(Task::getSubtaskIndex))
                .toList();
    }

    @Override
    public Page<Task> getTasksWithoutCourse(Specification<Task> spec, Pageable pageable) {
//        Page<Task> page = taskRepository.findParentTasksWithoutCourse(spec, pageable);
        Specification<Task> s = spec.and(TaskSpecifications.parentTaskIsNull())
                .and(TaskSpecifications.courseIsNull());
        Page<Task> page = taskRepository.findAll(s, pageable);
        log.info("found available tasks: {}", page);
        return page;
    }

    @Override
    public Page<Task> getTasksWithoutCourse(Pageable pageable) {
        Page<Task> page = taskRepository.findParentTasksWithoutCourse(pageable);
        log.info("found available tasks: {}", page);
        return page;
    }

    @Override
    public Page<Task> getParentTasks(Specification<Task> spec, Pageable pageable) {
//        Page<Task> page = taskRepository.findParentTasksAny(spec, pageable);
        Specification<Task> s = spec.and(TaskSpecifications.parentTaskIsNull())
                .and(TaskSpecifications.noCourseFirst());
        Page<Task> page = taskRepository.findAll(s, pageable);
        log.info("found available tasks: {}", page);
        return page;
    }

    @Override
    public Page<Task> getParentTasks(Pageable pageable){
        Page<Task> page = taskRepository.findParentTasksAny(pageable);
        log.info("found available tasks: {}", page);
        return page;
    }

    @Override
    public List<Task> addSubtasksToTask(Long taskId, TaskSubtaskListDTO dto) {
        log.info("adding subtasks to task: {}", taskId);
        Task task = getTaskById(taskId);
        for(Long id : dto.ids()) {
            Task subtask = getTaskById(id);
            subtask.setParentTask(task);
            taskRepository.save(subtask);
        }
        return getTaskSubtasks(taskId);
    }

    @Override
    public void removeSubtaskFromList(Long taskId, Long subtaskId) {
        log.info("removing subtask(id: {}) from list of task(id: {})", subtaskId, taskId);
//        Task task = getTaskById(taskId);
//        List<Task> subtasks = task.getSubtasks();
//        subtasks.remove(
//                subtasks.stream().filter(subtask -> subtask.getId().equals(subtaskId)).findFirst().orElse(new Task())
//        );
//        taskRepository.save(task);

        Task subtask = getTaskById(subtaskId);
        subtask.setParentTask(null);
        taskRepository.save(subtask);
    }

    @Override
    public void shuffleSubtasks(SubtaskShuffleRequest request) {
        log.info("request: {}", request);
        Task task = getTaskById(request.taskId());
        List<Task> subtasks = task.getSubtasks();
        log.info("old subtask order: {}", subtasks.stream().map(Task::getSubtaskIndex).toList().toString());
        Long[] subtaskIds = request.subtaskIds();
        for (int i = 0; i < subtaskIds.length; i++) {
            Long subtaskId = subtaskIds[i];
//            subtasks.set(i, copy.stream().filter(subtask -> subtask.getId().equals(subtaskId)).findAny().orElseThrow());
            subtasks.stream().filter(subtask -> subtask.getId().equals(subtaskId)).findAny().orElseThrow().setSubtaskIndex(i);
        }
        log.info("new subtask order: {}", subtasks.stream().map(Task::getSubtaskIndex).toList().toString());
        log.info("saving");
        taskRepository.saveAll(subtasks);
    }

    @Override
    public File generatePDF(Long taskId, String localeCode) throws IOException, URISyntaxException {
        Task task = getTaskById(taskId);
        return pdfService.generatePDF(task, TranslationService.getLocale(localeCode));
    }

    @Override
    public long getActiveTasksCount() {
        return taskRepository.getActiveTasksCount();
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
    public void createStudentTasksOnStudentCourseTransfer(Student student, Course course) {
        log.info("creating student tasks at the moment of transfering student(id:{}) to course(id:{})", student.getId(), course.getId());
        // clear student tasks which were not completed
        List<StudentTask> oldStudentTasks = student.getTasks().stream().toList();
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


    // LOCK TASKS AFTER YOU REMOVE STUDENT FROM COURSE
    @Override
    public void clearStudentTasksOnStudentDeletionFromCourse(Student student) {
//        student.getTasks().stream().filter(task -> task.getStatus() != StudentTaskStatus.COMPLETED)
//                .forEach(studentTaskRepository::delete);

        student.getTasks()
                .stream()
                .filter(task -> task.getStatus() == StudentTaskStatus.UNLOCKED || task.getStatus() == StudentTaskStatus.READY)
                .forEach(task -> {task.setStatus(StudentTaskStatus.LOCKED); studentTaskRepository.save(task);});
        log.info("locked non-completed student tasks (student id: {})", student.getId());
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

    @Override
    public List<StudentTaskLessonDTO> getOpenStudentTasks(Student student) {
        return student.getTasks().stream()
                .filter(task -> task.getStatus().equals(StudentTaskStatus.UNLOCKED) || task.getStatus().equals(StudentTaskStatus.READY))
                .sorted((o1,o2) -> Comparators.comparable().compare(o1.getTaskReference().getTaskIndex(), o2.getTaskReference().getTaskIndex()))
                .map(task -> {
                    Integer taskCompletionPercent = null;
                    try {
                        taskCompletionPercent = getTaskCompletionPercent(task);
                    } catch (TeamworkException ex) {
                        log.warn("could not get task(id:{}) completion percent: {}", task.getId(), ex.getMessage());
                    }
                    return new StudentTaskLessonDTO(
                            task.getId(),
                            task.getTaskReference().getTaskIndex(),
                            task.getTaskReference().getName(),
                            task.getStatus().name(),
                            taskCompletionPercent
                    );
                })
                .toList();
    }

    // up to 3 next tasks
    @Override
    public List<StudentTaskLessonDTO> getNextStudentTasks(Student student) {
        return student.getTasks().stream()
                .filter(task -> task.getStatus().equals(StudentTaskStatus.LOCKED))
                .sorted((o1,o2) -> Comparators.comparable().compare(o1.getTaskReference().getTaskIndex(), o2.getTaskReference().getTaskIndex()))
                .limit(3)
                .map(task -> {
                    Integer taskCompletionPercent = null;
                    try {
                        taskCompletionPercent = getTaskCompletionPercent(task);
                    } catch (TeamworkException ex) {
                        log.warn("could not get task(id:{}) completion percent: {}", task.getId(), ex.getMessage());
                    }
                    return new StudentTaskLessonDTO(
                            task.getId(),
                            task.getTaskReference().getTaskIndex(),
                            task.getTaskReference().getName(),
                            task.getStatus().name(),
                            taskCompletionPercent
                    );
                })
                .toList();
    }

    @Override
    public List<StudentTaskPointDTO> getStudentTaskProgressPoints(Long taskId) {
        StudentTask st = getStudentTask(taskId);
        List<TaskResponse> response = trackingService.getAllTasksFromList(st.getTaskTrackingId());
        List<StudentTaskPointDTO> points = response.stream().map(this::toPointDTO).toList();
        points.forEach(point -> {
            if(point.getParentTaskId() != null && point.getParentTaskId() != 0) {
                points.stream()
                        .filter(p -> p.getId().equals(point.getParentTaskId()))
                        .findAny()
                        .get()
                        .getSubpoints()
                        .add(point);
            }
        });

        return points.stream()
                .filter(point -> (point.getParentTaskId() == null || point.getParentTaskId() == 0))
                .toList();
    }

    private StudentTaskPointDTO toPointDTO(TaskResponse task) {
        List<StudentTaskTagDTO> tags = new ArrayList<>();
        if(task.tagIds() != null && task.tagIds().length > 0) {
            for(Long tagId : task.tagIds()) {
                tags.add(
                        Optional.of(trackingService.getTagById(String.valueOf(tagId)))
                                .map(
                                        tag -> new StudentTaskTagDTO(
                                                tag.id(),
                                                tag.name(),
                                                tag.color()
                                        )
                                ).get()
                );
            }
        }
        return new StudentTaskPointDTO(
                task.id(),
                task.parentTaskId(),
                task.tasklistId(),
                task.name(),
                task.description(),
                task.priority(),
                task.progress(),
                task.startDate(),
                task.status(),
                trackingService.getTotalTimeOnTask(String.valueOf(task.id())).taskTimeTotal().minutes(),
                trackingService.getTotalTimeOnTask(String.valueOf(task.id())).taskTimeTotal().estimatedMinutes(),
                tags,
                new ArrayList<>());
    }

    private Integer getTaskCompletionPercent(StudentTask st) {
        List<TaskResponse> points = trackingService.getAllTasksFromList(st.getTaskTrackingId());
        return (int) (
                100.0 * points.stream().filter(p -> p.status().equalsIgnoreCase("completed")).count()
                /
                points.size()
        );
    }

    @Override
    @Transactional
    public void lockStudentTask(Long taskID) {
        StudentTask task = studentTaskRepository.findById(taskID).orElseThrow();
        if(task.getStudent().isEnabled()) {
            task.getTaskReference().getActiveStudents().remove(task.getStudent());
            task.setStatus(StudentTaskStatus.LOCKED);
            task.setBeginDate(null);
            task.setEndDate(null);
            studentTaskRepository.save(task);
            log.info("task locked");
        }
        else {
            log.error("attempt to edit task of blocked student");
            throw new BlockedEntityException();
        }
    }

    @Override
    @Transactional
    public void unlockStudentTask(Long taskID) {
        StudentTask task = studentTaskRepository.findById(taskID).orElseThrow();
        if(task.getStudent().isEnabled()) {
            task.getTaskReference().getActiveStudents().add(task.getStudent());
            task.setStatus(StudentTaskStatus.UNLOCKED);
            task.setBeginDate(ZonedDateTime.now());
            task.setEndDate(null);
            studentTaskRepository.save(task);
            log.info("task unlocked");

            if(
                    isNull(task.getTaskTrackingId())
                            && nonNull(task.getTaskReference().getCourse())
                            && nonNull(task.getTaskReference().getCourse().getTrackingId())
                            && nonNull(task.getStudent().getTaskTrackingProfileId())
            ) {
                createTrackingList(task);
            }
        }
        else {
            log.error("attempt to edit task of blocked student");
            throw new BlockedEntityException();
        }
    }

    @Async
    private void createTrackingList(StudentTask task) {
        log.info("creating tracking list for task");
        TaskListResponse response = trackingService.createTaskList(new TaskListRequest(
                task.getTaskReference().getCourse().getTrackingId(),
                null,
                false,
                new TaskListDescription(
                        task.getTaskReference().getName(),
                        String.format("%s [%s]",
                                task.getTaskReference().getName(),
                                task.getStudent().getInitials())
                )
        ));
        task.setTaskTrackingId(response.taskListId());
        studentTaskRepository.save(task);
        log.info("created tracking list for task, creating progress points");
        task.getTaskReference().getTaskProgressPoints().forEach(point -> {
            TaskResponse createdParentTask = trackingService.createTaskInTaskList(
                    new TaskRequest(
                            null,
                            point.getName(),
                            point.getName(),
                            null,
                            0,
                            new Long[]{trackingService.getTagByName(trackingService.getRecommendedTagName()).id()},
                            null,
                            LocalDate.now(),
                            null,
                            Long.valueOf(task.getTaskTrackingId()),
                            new Integer[]{Integer.valueOf(task.getStudent().getTaskTrackingProfileId())}
                    )
            );
            point.getSubpoints().forEach(subpoint -> trackingService.createTaskInTaskList(
                    new TaskRequest(
                            null,
                            subpoint.getName(),
                            subpoint.getName(),
                            null,
                            0,
                            new Long[]{trackingService.getTagByName(trackingService.getRecommendedTagName()).id()},
                            null,
                            LocalDate.now(),
                            createdParentTask.id(),
                            Long.valueOf(task.getTaskTrackingId()),
                            new Integer[]{Integer.valueOf(task.getStudent().getTaskTrackingProfileId())}
                    )
            ));
        });
    }

    @Async
    private void updateTrackingList(StudentTask task) {
        log.info("creating tracking list for task");
        trackingService.updateTaskList(new TaskListRequest(
                task.getTaskReference().getCourse().getTrackingId(),
                Long.valueOf(task.getTaskTrackingId()),
                false,
                new TaskListDescription(
                        task.getTaskReference().getName(),
                        String.format("%s [%s]",
                                task.getTaskReference().getName(),
                                task.getStudent().getInitials())
                )
        ));
    }

    @Override
    @Transactional
    public void completeStudentTask(Long taskID) {
        StudentTask task = studentTaskRepository.findById(taskID).orElseThrow();
        if(task.getStudent().isEnabled()) {
            task.getTaskReference().getActiveStudents().remove(task.getStudent());
            task.setStatus(StudentTaskStatus.COMPLETED);
            task.setEndDate(ZonedDateTime.now());
            studentTaskRepository.save(task);
            log.info("task completed");
        }
        else {
            log.error("attempt to edit task of blocked student");
            throw new BlockedEntityException();
        }

    }

    @Override
    @Transactional
    public void resetStudentTask(Long taskID) {
        StudentTask task = studentTaskRepository.findById(taskID).orElseThrow();
        if(task.getStudent().isEnabled()) {
            task.getTaskReference().getActiveStudents().add(task.getStudent());
            task.setStatus(StudentTaskStatus.UNLOCKED);
            task.setBeginDate(ZonedDateTime.now());
            task.setEndDate(null);
            studentTaskRepository.save(task);
            log.info("task reset");
        }
        else {
            log.error("attempt to edit task of blocked student");
            throw new BlockedEntityException();
        }
    }

    @Override
    public Specification<StudentTask> buildSpec(FilterForm filters) {
        Long id = filters.getId();
        Long student = filters.getStudent();
        String name = filters.getName();
        Long courseID = Optional.ofNullable(filters.getCourse()).orElse(-1L);
        String statusInput = filters.getStatus();
        // todo add dates

//        List<StudentTaskStatus> statusList = new ArrayList<>();
//        if(statusInput == null) {
//            statusList.add(StudentTaskStatus.UNLOCKED);
//            statusList.add(StudentTaskStatus.READY);
//        }
//        else if(statusInput.contains(",")) {
//            for(String statusString : statusInput.split(",")) {
//                statusList.add(StudentTaskStatus.valueOf(statusString));
//            }
//        }
//        else statusList.add(StudentTaskStatus.valueOf(statusInput));

        StudentTaskStatus status = ValidationUtils.fieldIsEmpty(statusInput)
                ? StudentTaskStatus.UNLOCKED
                : StudentTaskStatus.valueOf(statusInput);

        Specification<StudentTask> spec =
                hasCourseID(courseID <= 0 ? null : courseID)
                .and(hasStudentId(student))
                .and(hasNameLike(name))
                .and(hasId(id))
                .and(
                        status.equals(StudentTaskStatus.UNLOCKED)
                        ? hasStatus(status).or(hasStatus(StudentTaskStatus.READY))
                        : hasStatus(status)
                );

        return spec;
    }

    @Override
    public Specification<Task> buildSpecificationFromFilters(FilterForm filters) {
        log.info("Building specification from filters: " + filters);

        Long id = filters.getId();
        String name = filters.getName();
        Long courseID = filters.getCourse();
        String levelInput = filters.getLevel();
        String statusInput = filters.getStatus();

        Course course = NumericUtils.fieldIsEmpty(courseID) ? null : courseRepository.getReferenceById(courseID);
        TaskStatus status = StringUtils.fieldIsEmpty(statusInput) ? null : TaskStatus.valueOf(statusInput);
        TaskLevel level = StringUtils.fieldIsEmpty(levelInput) ? null : TaskLevel.valueOf(levelInput);

        Specification<Task> spec = Specification.where(TaskSpecifications.hasNameLike(name))
                                    .and(TaskSpecifications.hasCourse(course))
                                    .and(TaskSpecifications.hasId(id))
                                    .and(TaskSpecifications.hasLevel(level))
                                    .and(TaskSpecifications.hasStatus(status));

        return spec;
    }
}
