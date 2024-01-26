package com.example.spacelab.service.impl;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.example.spacelab.dto.course.CourseEditDTO;
import com.example.spacelab.dto.course.CourseIconDTO;
import com.example.spacelab.dto.course.StudentCourseTaskInfoDTO;
import com.example.spacelab.dto.student.StudentAvatarDTO;
import com.example.spacelab.dto.task.TaskCourseDTO;
import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.exception.TeamworkException;
import com.example.spacelab.integration.TaskTrackingService;
import com.example.spacelab.integration.data.*;
import com.example.spacelab.mapper.CourseMapper;
import com.example.spacelab.mapper.TaskMapper;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.course.CourseStatus;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.student.StudentAccountStatus;
import com.example.spacelab.model.student.StudentTask;
import com.example.spacelab.model.student.StudentTaskStatus;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.model.task.TaskStatus;
import com.example.spacelab.repository.*;
import com.example.spacelab.service.CourseService;
import com.example.spacelab.service.FileService;
import com.example.spacelab.service.StudentService;
import com.example.spacelab.service.StudentTaskService;
import com.example.spacelab.service.specification.CourseSpecifications;
import com.example.spacelab.util.FilenameUtils;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.util.NumericUtils;
import com.example.spacelab.util.ValidationUtils;
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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Slf4j
@Service
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
        else return getCourseById(id).getTasks().stream().toList();
    }

    @Override
    @Transactional
    public StudentCourseTaskInfoDTO getStudentCourseInfo(Long studentID) {
        Student student = studentService.getStudentById(studentID);
        Course studentCourse = student.getCourse();
        return new StudentCourseTaskInfoDTO(
                studentCourse.getId(),
                studentCourse.getName(),
                studentCourse.getIcon(),
                student.getTasks()
                        .stream()
                        .filter(st -> st.getTaskReference().getStatus().equals(TaskStatus.ACTIVE))
                        .map(taskMapper::fromStudentTaskToDTO)
                        .toList()
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

        CourseStatus courseStatus = (ValidationUtils.fieldIsEmpty(status)) ? null : CourseStatus.valueOf(status);

        Admin mentor = ValidationUtils.fieldIsEmpty(mentorID) ? null : adminRepository.getReferenceById(mentorID);
        Admin manager = ValidationUtils.fieldIsEmpty(managerID) ? null : adminRepository.getReferenceById(managerID);

        LocalDate beginDate = (ValidationUtils.fieldIsNotEmpty(beginDateString)) ? LocalDate.parse(beginDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null;
        LocalDate endDate = (ValidationUtils.fieldIsNotEmpty(endDateString)) ? LocalDate.parse(endDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null;

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
        c.getMentor().getCourses().add(c);
        c.getManager().getCourses().add(c);
        updateCourseTaskList(c, dto);
        updateCourseStudents(c, dto);
        updateStudentsCourseTaskList(c);
        try {
            createTrackingCourseProject(c);
            addStudentsToProject(
                    c.getStudents().stream().map(Student::getTaskTrackingProfileId).filter(Objects::nonNull).toList(),
                    c.getTrackingId()
            );
        } catch (TeamworkException ex) {
            log.error("could not update tracking project: {}", ex.getMessage());
        }
        return courseRepository.save(c);
    }

    @Override
    public Course editCourse(CourseEditDTO dto) {
        log.info("removing admins from old course reference");
        removeAdminsFromCourse(dto.getId());

        log.info("editing course");
        Course c = mapper.fromEditDTOToCourse(dto);
        Set<Student> oldStudentList = c.getStudents();
        Admin mentor = c.getMentor();
        Admin manager = c.getManager();
//        log.info(mentor.toString());
//        log.info(manager.toString());
        mentor.getCourses().add(c);
        manager.getCourses().add(c);
//        log.info("after adding courses");
//        log.info(mentor.getCourses().stream().map(Course::getId).map(Object::toString).collect(Collectors.joining(",")));
//        log.info(manager.getCourses().stream().map(Course::getId).map(Object::toString).collect(Collectors.joining(",")));
        updateCourseTaskList(c, dto);
        updateCourseStudents(c, dto);
        updateStudentsCourseTaskList(c);
        Set<Student> newStudentList = c.getStudents();

        try {
            updateTrackingCourseProject(c);
        } catch (TeamworkException ex) {
            log.error("could not update tracking project: {}", ex.getMessage());
        }
        try {
            updateStudentsInProject(oldStudentList, newStudentList, c.getTrackingId());
        } catch (TeamworkException ex) {
            log.error("could not update students in project: {}", ex.getMessage());
        }

        return courseRepository.save(c);
    }

    @Override
    public void removeAdminsFromCourse(Long courseId) {
        Course oldCourse = getCourseById(courseId);
        Admin mentor = oldCourse.getMentor();
        Admin manager = oldCourse.getManager();
        log.info(mentor.toString());
        log.info(manager.toString());
        log.info("before deleting courses");
        log.info(mentor.getCourses().stream().map(Course::getId).map(Object::toString).collect(Collectors.joining(",")));
        log.info(manager.getCourses().stream().map(Course::getId).map(Object::toString).collect(Collectors.joining(",")));
        mentor.getCourses().remove(oldCourse);
        manager.getCourses().remove(oldCourse);
        log.info("after deleting courses");
        log.info(mentor.getCourses().stream().map(Course::getId).map(Object::toString).collect(Collectors.joining(",")));
        log.info(manager.getCourses().stream().map(Course::getId).map(Object::toString).collect(Collectors.joining(",")));
        adminRepository.save(mentor);
        adminRepository.save(manager);
        courseRepository.save(oldCourse);
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

    @Async
    private void addStudentsToProject(List<String> studentIds, String projectId) {
        log.info("Adding students(ids:{}) to project(id:{})", studentIds.toString(), projectId);
        UserAddResponse response = trackingService.addUsersToProject(
                new UserAddRequest(
                        projectId,
                        studentIds.stream().map(Integer::parseInt).toArray(Integer[]::new)
                )
        );
        log.info(response.toString());
    }

    @Async
    private void updateStudentsInProject(Set<Student> oldList, Set<Student> newList, String projectId) {
        List<Student> studentsToAdd = newList.stream().filter(st -> !oldList.contains(st)).toList();
        List<Student> studentsToRemove = oldList.stream().filter(st -> !newList.contains(st)).toList();

        addStudentsToProject(
                studentsToAdd.stream().map(Student::getTaskTrackingProfileId).filter(Objects::nonNull).toList(),
                projectId
        );
        removeStudentsFromProject(
                studentsToRemove.stream().map(Student::getTaskTrackingProfileId).filter(Objects::nonNull).toList(),
                projectId
        );
    }

    @Async
    private void removeStudentsFromProject(List<String> studentIds, String projectId) {
        log.info("Removing students(ids:{}) from project(id:{})", studentIds.toString(), projectId);
        UserRemoveResponse response = trackingService.removeUsersFromProject(new UserRemoveRequest(projectId, new UserRemoveRequest.Remove(
                String.join(",", studentIds)
        )));
        log.info(response.toString());
    }


    @Transactional
    private void updateCourseTaskList(Course c, CourseEditDTO dto) {
        log.info("updating course task list");

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

    @Transactional
    private void updateCourseStudents(Course c, CourseEditDTO dto) {
        log.info("updating course students");
        List<Student> courseStudents = new ArrayList<>(c.getStudents().stream().toList());

        List<Long> oldStudentIds = courseStudents.stream().map(Student::getId).toList();
        List<Long> updatedStudentsIds = dto.getMembers().getStudents().stream().map(StudentAvatarDTO::getId).toList();

        // ids of old students to remove from course
        courseStudents.stream()
                .filter(st -> !updatedStudentsIds.contains(st.getId()))
                .forEach(st -> {
                    st.setCourse(null);
                    st.getDetails().setAccountStatus(StudentAccountStatus.INACTIVE);
                    studentTaskService.clearStudentTasksOnStudentDeletionFromCourse(st);
                });

        // ids of new students to add to course
        updatedStudentsIds.stream()
                .filter(newId -> !oldStudentIds.contains(newId))
                .forEach(newId -> {
                        studentRepository.findById(newId).ifPresent(foundStudent -> {
                            log.info("adding student(id:{}) to course", newId);
                            foundStudent.setCourse(c);
                            foundStudent.getDetails().setAccountStatus(StudentAccountStatus.ACTIVE);
                            Student st = studentRepository.save(foundStudent);
                            courseStudents.add(st);
                    });
                });

        // if course is inactive = disable all its students
        c.getStudents().forEach(st -> {
            if(c.getStatus().equals(CourseStatus.ACTIVE) && !st.getDetails().getAccountStatus().equals(StudentAccountStatus.ACTIVE)) {
                st.getDetails().setAccountStatus(StudentAccountStatus.ACTIVE);
            }
            else if(c.getStatus().equals(CourseStatus.INACTIVE) && st.getDetails().getAccountStatus().equals(StudentAccountStatus.ACTIVE)) {
                st.getDetails().setAccountStatus(StudentAccountStatus.INACTIVE);
            }
            else {
                // in case more course statuses are added
            }
        });

    }

    // tasks which are removed from course structure list -> clear student task copies ???
    // tasks which are added to course structure -> create new student tasks for them
    @Transactional
    private void updateStudentsCourseTaskList(Course c) {

        List<Long> courseTasksIds = c.getTasks().stream().map(Task::getId).toList();
        List<Student> courseStudents = c.getStudents().stream().toList();
        courseStudents.forEach(student -> {

            /*
            fixme
            delete student tasks - not necessary

             student.getTasks().stream()
                    .filter(studentTask -> (
                            !courseTasksIds.contains(studentTask.getTaskReference().getId())
                            && studentTask.getStatus() != StudentTaskStatus.COMPLETED
                    ))
                    .forEach(studentTaskRepository::delete);
             */


            List<Long> studentTaskIds = student.getTasks().stream().map(st -> st.getTaskReference().getId()).toList();

            courseTasksIds.stream()
                    .filter(newId -> !studentTaskIds.contains(newId))
                    .forEach(newId -> taskRepository.findById(newId).ifPresent(foundTask -> {
                        log.info("student(id:{}) does not have task(id:{}), creating", student.getId(), newId);
                        StudentTask st = studentTaskService.fromTaskToStudentTask(foundTask);
                        st.setStudent(student);
                        student.getTasks().add(studentTaskRepository.save(st));
                    }));
        });
    }

    @Override
    @Transactional
    public void deleteCourseById(Long id) {
        Course c = getCourseById(id);
        c.getMentor().getCourses().remove(c);
        c.getManager().getCourses().remove(c);
        c.setManager(null);
        c.setMentor(null);
        c.getStudents().forEach(st -> st.setCourse(null));
        c.getStudents().clear();
        c.getTasks().forEach(t -> t.setCourse(null));
        c.getTasks().clear();
        c.getLiterature().forEach(l -> l.setCourse(null));
        c.getLiterature().clear();
        deleteTrackingProject(c.getTrackingId());
        courseRepository.delete(c);
    }

    @Async
    private void deleteTrackingProject(String id) {
        try {
            trackingService.deleteProject(id);
            log.info("Deleted tracking project (tracking id {})", id);
        } catch (TeamworkException ex) {
            log.error("Could not delete tracking project (tracking id {}), errors: {}", id, ex.getMessage());
        }
    }

    @Override
    public void saveIcon(Long id, CourseIconDTO dto) throws IOException {
        MultipartFile file = dto.icon();
        if(file.getSize() > 0) {
            Course c = getCourseById(id);
            String filename = FilenameUtils.generateFileName(file);
            try {
                fileService.saveFile(file, filename,"courses");
                c.setIcon(filename);
                courseRepository.save(c);
            } catch (AmazonS3Exception ex) {
                log.error("could not save icon: {}", ex.getMessage());
            }

        }
    }

    @Override
    public void deleteIcon(Long id) throws IOException {
        Course c = getCourseById(id);
        c.setIcon(null);
        courseRepository.save(c);
    }

}
