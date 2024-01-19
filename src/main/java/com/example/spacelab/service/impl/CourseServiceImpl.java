package com.example.spacelab.service.impl;

import com.example.spacelab.dto.course.CourseEditDTO;
import com.example.spacelab.dto.course.CourseIconDTO;
import com.example.spacelab.dto.course.StudentCourseTaskInfoDTO;
import com.example.spacelab.dto.student.StudentAvatarDTO;
import com.example.spacelab.dto.task.TaskCourseDTO;
import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.integration.TaskTrackingService;
import com.example.spacelab.integration.data.ProjectRequest;
import com.example.spacelab.integration.data.ProjectResponse;
import com.example.spacelab.mapper.CourseMapper;
import com.example.spacelab.mapper.TaskMapper;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.course.CourseStatus;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.student.StudentTask;
import com.example.spacelab.model.student.StudentTaskStatus;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.repository.*;
import com.example.spacelab.service.CourseService;
import com.example.spacelab.service.FileService;
import com.example.spacelab.service.StudentService;
import com.example.spacelab.service.StudentTaskService;
import com.example.spacelab.service.specification.CourseSpecifications;
import com.example.spacelab.util.FilenameUtils;
import com.example.spacelab.util.FilterForm;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final StudentTaskRepository studentTaskRepository;
    private final StudentRepository studentRepository;
    private final TaskRepository taskRepository;

    private final StudentService studentService;
    private final StudentTaskService studentTaskService;

    private final AdminRepository adminRepository;
    private final CourseRepository courseRepository;

    private final CourseMapper mapper;
    private final TaskMapper taskMapper;

    private final TaskTrackingService trackingService;

    private final FileService fileService;

    @Override
    public List<Course> getCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Page<Course> getCourses(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    @Override
    public Page<Course> getCourses(FilterForm filters, Pageable pageable) {
        Specification<Course> spec = buildSpecificationFromFilters(filters);

        return courseRepository.findAll(spec, pageable);
    }

    @Override
    public List<Course> getAllowedCourses(Long... ids) {
        return courseRepository.findAllowedCourses(ids);
    }

    @Override
    public Page<Course> getAllowedCourses(Pageable pageable, Long... ids) {
        return courseRepository.findAllowedCoursesPage(pageable, ids);
    }

    @Override
    public Page<Course> getAllowedCourses(FilterForm filters, Pageable pageable, Long... ids) {
        Specification<Course> spec = buildSpecificationFromFilters(filters).and(CourseSpecifications.hasCourseIDs(ids));
        return courseRepository.findAll(spec, pageable);
    }

    @Override
    public Page<Course> getCoursesByName(String name, Pageable pageable) {
        FilterForm filters = new FilterForm();
        filters.setName(name);
        Specification<Course> spec = buildSpecificationFromFilters(filters);
        return courseRepository.findAll(spec, pageable);
    }

    @Override
    public List<Task> getCourseTasks(Long id) {
        if(id == null) return new ArrayList<>();
        else return getCourseById(id).getTasks();
    }

    @Override
    public StudentCourseTaskInfoDTO getStudentCourseInfo(Long studentID) {
        Student student = studentService.getStudentById(studentID);
        Course studentCourse = student.getCourse();
        return new StudentCourseTaskInfoDTO(
                studentCourse.getId(),
                studentCourse.getName(),
                studentCourse.getIcon(),
                student.getTasks().stream().map(taskMapper::fromStudentTaskToDTO).toList()
        );
    }

    @Override
    public Specification<Course> buildSpecificationFromFilters(FilterForm filters) {

        log.info("Building specification from filters: " + filters);

        String name = filters.getName();
        String dateString = filters.getDate();
        String beginDateString = filters.getBegin();
        String endDateString = filters.getEnd();
        Long mentorID = filters.getMentor();
        Long managerID = filters.getManager();
        String status = filters.getStatus();

        CourseStatus courseStatus = (status == null || status.isEmpty()) ? null : CourseStatus.valueOf(status);

        Admin mentor = (mentorID == null) ? null : adminRepository.getReferenceById(mentorID);
        Admin manager = (managerID == null) ? null : adminRepository.getReferenceById(managerID);

        LocalDate beginDate = (beginDateString != null && !beginDateString.isEmpty()) ? LocalDate.parse(beginDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null;
        LocalDate endDate = (endDateString != null && !endDateString.isEmpty()) ? LocalDate.parse(endDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null;

        Specification<Course> spec = Specification.where(
                        CourseSpecifications.hasNameLike(name)
                        .and(CourseSpecifications.hasMentor(mentor))
                        .and(CourseSpecifications.hasManager(manager))
                        .and(CourseSpecifications.hasStatus(courseStatus)
                        .and(CourseSpecifications.hasDatesBetween(beginDate, endDate)))
        );
        return spec;
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    @Override
    public Course createNewCourse(CourseEditDTO dto) {
        log.info("creating new course");
        Course c = mapper.fromEditDTOToCourse(dto);
        c.setStatus(CourseStatus.ACTIVE);
        c = courseRepository.save(c);
        updateCourseTaskList(c, dto);
        updateCourseStudents(c, dto);
        return courseRepository.save(c);
    }

    @Override
    public Course editCourse(CourseEditDTO dto) {
        log.info("editing course");
        Course c = mapper.fromEditDTOToCourse(dto);
        updateCourseTaskList(c, dto);
        updateCourseStudents(c, dto);
        updateStudentsCourseTaskList(c);
        return courseRepository.save(c);
    }

    @Async
    @Override
    public void createTrackingCourseProject(Course course) {
        log.info("creating tracking project");
        ProjectResponse response = trackingService.createProject(new ProjectRequest(
                null,
                course.getName(),
                course.getName() + " - Description"
        ));
        course.setTrackingId(response.id());
        log.info("tracking id set: {}", response.id());
    }

    @Async
    @Override
    public void updateTrackingCourseProject(Course course) {
        log.info("updating tracking project for course: {}", course.getId());
        trackingService.updateProject(new ProjectRequest(
                course.getTrackingId(),
                course.getName(),
                course.getName() + " - Description"
        ));
    }

    // fixme maybe later
    private void updateCourseStudents(Course c, CourseEditDTO dto) {
        log.info("updating course students");
        List<Student> courseStudents = c.getStudents();
//        courseStudents.forEach(student -> student.setCourse(null));
//        courseStudents.clear();

        List<Long> oldStudentIds = courseStudents.stream().map(Student::getId).toList();
        List<Long> updatedStudentsIds = dto.getMembers().getStudents().stream().map(StudentAvatarDTO::getId).toList();

        // ids of old students to remove from course
        courseStudents.stream()
                .filter(st -> !updatedStudentsIds.contains(st.getId()))
                .forEach(st -> {st.setCourse(null); studentTaskService.clearStudentTasksOnStudentDeletionFromCourse(st);});

        // ids of new students to add to course
        updatedStudentsIds.stream()
                .filter(newId -> !oldStudentIds.contains(newId))
                .forEach(newId -> {
                        studentRepository.findById(newId).ifPresent(foundStudent -> {
                            foundStudent.setCourse(c);
                            Student st = studentRepository.save(foundStudent);
                            courseStudents.add(st);
                            studentTaskService.createStudentTasksOnStudentCourseTransfer(st, c);
                    });
                });

    }

    // fixme maybe later
    private void updateCourseTaskList(Course c, CourseEditDTO dto) {
        log.info("updating course task list");
//        List<Task> oldTaskList = taskRepository.getCourseTasks(c.getId());
//        log.info("old task list: {}", oldTaskList.stream().map(Task::getId).toList());
//        oldTaskList.forEach(task -> {task.setCourse(null); taskRepository.save(task);});

        c.getTasks().forEach(t -> t.setCourse(null));
        c.getTasks().clear();

        log.info(dto.getStructure().getTasks().toString());

        dto.getStructure().getTasks().forEach(newCourseTask -> {
            taskRepository.findById(newCourseTask.getId()).ifPresent(foundTask -> {
                foundTask.setTaskIndex(newCourseTask.getTaskIndex());
                foundTask.setCourse(c);
                taskRepository.save(foundTask);
                c.getTasks().add(foundTask);
            });
        });
        List<Task> newTaskList = taskRepository.getCourseTasks(c.getId());
        log.info("new task list: {}", newTaskList.stream().map(Task::getId).toList());
    }

    // fixme maybe later
    // tasks which are removed from course structure list -> clear student task copies
    // tasks which are added to course structure -> create new student tasks for them
    private void updateStudentsCourseTaskList(Course c) {

        List<Long> courseTasksIds = c.getTasks().stream().map(Task::getId).toList();
        List<Student> courseStudents = c.getStudents();
        courseStudents.forEach(student -> {

            student.getTasks().stream()
                    .filter(studentTask -> (
                            !courseTasksIds.contains(studentTask.getTaskReference().getId())
                            && studentTask.getStatus() != StudentTaskStatus.COMPLETED
                    ))
                    .forEach(studentTaskRepository::delete);

            List<Long> studentTaskIds = student.getTasks().stream().map(st -> st.getTaskReference().getId()).toList();

            courseTasksIds.stream()
                    .filter(newId -> !studentTaskIds.contains(newId))
                    .forEach(newId -> {
                        taskRepository.findById(newId).ifPresent(foundTask -> {
                            StudentTask st = studentTaskService.fromTaskToStudentTask(foundTask);
                            st.setStudent(student);
                            studentTaskRepository.save(st);
                        });
                    });
        });
    }

    @Override
    public void deleteCourseById(Long id) {
        Course c = getCourseById(id);
        c.getStudents().forEach(st -> {
            st.setCourse(null);
        });
        c.getStudents().clear();
        c.setManager(null);
        c.setMentor(null);
        c.getTasks().forEach(t -> t.setCourse(null));
        c.getTasks().clear();
        courseRepository.delete(c);
    }

    @Override
    public void saveIcon(Long id, CourseIconDTO dto) throws IOException {
        MultipartFile file = dto.icon();
        if(file.getSize() > 0) {
            String filename = FilenameUtils.generateFileName(file);
            fileService.saveFile(file, filename,"courses");
            Course c = getCourseById(id);
            c.setIcon(filename);
            courseRepository.save(c);
        }
    }

    @Override
    public void deleteIcon(Long id) throws IOException {
        Course c = getCourseById(id);
        c.setIcon(null);
        courseRepository.save(c);
    }

}
