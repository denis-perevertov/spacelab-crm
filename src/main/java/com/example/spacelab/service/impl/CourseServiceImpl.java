package com.example.spacelab.service.impl;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.example.spacelab.dto.course.CourseEditDTO;
import com.example.spacelab.dto.course.CourseIconDTO;
import com.example.spacelab.dto.course.StudentCourseTaskInfoDTO;
import com.example.spacelab.dto.statistics.CourseStatisticsDTO;
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
import com.example.spacelab.model.task.Task;
import com.example.spacelab.model.task.TaskStatus;
import com.example.spacelab.repository.*;
import com.example.spacelab.service.*;
import com.example.spacelab.service.specification.CourseSpecifications;
import com.example.spacelab.util.FilenameUtils;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.util.ValidationUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.comparator.Comparators;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private final LessonRepository lessonRepository;

    private final CourseMapper mapper;
    private final TaskMapper taskMapper;

    private final TaskTrackingService trackingService;

    private final FileService fileService;

    private final NotificationService notificationService;

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
                        .filter(st -> st.getTaskReference().getStatus().equals(TaskStatus.ACTIVE) && st.getTaskReference().getCourse().getId().equals(studentCourse.getId()))
                        .sorted((o1, o2) -> Comparators.comparable().compare(o1.getTaskReference().getTaskIndex(), o2.getTaskReference().getTaskIndex()))
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
        if(c.getMentor() != null) {
            c.getMentor().getCourses().add(c);
            notificationService.sendAssignedToNewCourseNotification(c);
        }
        if(c.getManager() != null) {
            c.getManager().getCourses().add(c);
            notificationService.sendAssignedToNewCourseNotification(c);
        }
        updateCourseTaskList(c, dto);
        updateCourseStudents(c, dto);
        updateStudentsCourseTaskList(c);

        try {
            createTrackingCourseProject(c);
        } catch (Exception ex) {
            log.error("could not update tracking project: {}", ex.getMessage());
        }
        try {
            addStudentsToProject(
                    c.getStudents().stream().map(Student::getTaskTrackingProfileId).filter(Objects::nonNull).toList(),
                    c.getTrackingId()
            );
        } catch (Exception ex) {
            log.error("could not add students to project: {}", ex.getMessage());
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
        if(mentor != null) {
            mentor.getCourses().add(c);
            if(!mentor.getCourses().contains(c)) {
                notificationService.sendAssignedToNewCourseNotification(c);
            }
        }
        if(manager != null) {
            manager.getCourses().add(c);
            if(!manager.getCourses().contains(c)) {
                notificationService.sendAssignedToNewCourseNotification(c);
            }
        }
        updateCourseTaskList(c, dto);
        updateCourseStudents(c, dto);
        updateStudentsCourseTaskList(c);
        Set<Student> newStudentList = c.getStudents();

        try {
            updateTrackingCourseProject(c);
        } catch (Exception ex) {
            log.error("could not update tracking project: {}", ex.getMessage());
        }
        try {
            updateStudentsInProject(oldStudentList, newStudentList, c.getTrackingId());
        } catch (Exception ex) {
            log.error("could not update students in project: {}", ex.getMessage());
        }

        return courseRepository.save(c);
    }

    @Override
    public void removeAdminsFromCourse(Long courseId) {
        Course oldCourse = getCourseById(courseId);
        Admin mentor = oldCourse.getMentor();
        Admin manager = oldCourse.getManager();
        Optional.ofNullable(mentor).ifPresent(m -> {
            m.getCourses().remove(oldCourse);
            adminRepository.save(m);
        });
        Optional.ofNullable(manager).ifPresent(m -> {
            m.getCourses().remove(oldCourse);
            adminRepository.save(m);
        });
        courseRepository.save(oldCourse);
    }

    @Override
    public List<Course> getAdminCourses(Long adminId) {
        return courseRepository.findAllAdminCourses(adminId);
    }


    //fixme
    @Override
    @Transactional
    public List<CourseStatisticsDTO> getCoursesByStudentRating(int limit, Sort.Direction direction) {
//        Comparator<Course> studentRatingComparator = (c1, c2) -> {
//            double avg1 = c1.getStudents().stream().mapToInt(Student::getRating).average().orElse(0.0);
//            double avg2 = c2.getStudents().stream().mapToInt(Student::getRating).average().orElse(0.0);
//            return (int) (direction.isAscending() ? avg1 - avg2 : avg2 - avg1);
//        };
        return courseRepository.findAll()
                .stream()
                .map(c -> new CourseStatisticsDTO(c.getName(), new Object[]{c.getStudents().stream().mapToInt(Student::getRating).average().orElse(0.0)}))
                .sorted((c1,c2) -> direction.isAscending() ? (int) ((double)c1.values()[0] - (double)c2.values()[0]) : (int)((double)c2.values()[0] - (double)c1.values()[0]))
                .limit(limit)
                .toList();
    }

    @Override
    @Transactional
    public List<CourseStatisticsDTO> getCoursesByHiredStudents(int limit, Sort.Direction direction) {
        return courseRepository.findAll()
                .stream()
                .map(c -> {
                    int hiredCount = 0;
                    int blockedCount = 0;
                    int expelledCount = 0;
                    int inactiveCount = 0;
                    int activeCount = 0;
                    for(Student s : c.getStudents()) {
                        switch (s.getDetails().getAccountStatus()) {
                            case ACTIVE -> activeCount++;
                            case INACTIVE -> inactiveCount++;
                            case EXPELLED -> expelledCount++;
                            case BLOCKED -> blockedCount++;
                            case HIRED -> hiredCount++;
                        }
                    }
                    return new CourseStatisticsDTO(
                            c.getName(),
                            new Object[]{hiredCount, blockedCount, expelledCount, inactiveCount, activeCount}
                    );
                })
                .sorted((c1,c2) -> direction.isAscending() ? (int)c1.values()[0] - (int)c2.values()[0] : (int)c2.values()[0] - (int)c1.values()[0])
                .limit(limit)
                .toList();
    }

    /*
    Difficulty Rating
    ADVANCED - 3 points
    INTERMEDIATE - 2 points
    BEGINNER - 1 point
     */

    @Override
    @Transactional
    public List<CourseStatisticsDTO> getCoursesByDifficulty(int limit, Sort.Direction direction) {
        return courseRepository.findAll()
                .stream()
                .map(c -> {
                    int beginnerTasks = 0;
                    int intermediateTasks = 0;
                    int advancedTasks = 0;
                    for(Task t : c.getTasks()) {
                        switch (t.getLevel()) {
                            case BEGINNER -> beginnerTasks++;
                            case INTERMEDIATE -> intermediateTasks++;
                            case ADVANCED -> advancedTasks++;
                        }
                    }
                    return new CourseStatisticsDTO(
                            c.getName(),
                            new Object[]{advancedTasks, intermediateTasks, beginnerTasks}
                    );
                })
                .sorted((c1,c2) -> {
                    int totalRating1 = (int)c1.values()[0]*3 + (int)c1.values()[1]*2 + (int)c1.values()[2];
                    int totalRating2 = (int)c2.values()[0]*3 + (int)c2.values()[1]*2 + (int)c2.values()[2];
                    return direction.isAscending() ? totalRating1 - totalRating2 : totalRating2 - totalRating1;
                })
                .limit(limit)
                .toList();
    }

    @Override
    public List<CourseStatisticsDTO> getCoursesByLessonCount(int limit, Sort.Direction direction) {
        return courseRepository.findAll()
                .stream()
                .map(c -> new CourseStatisticsDTO(c.getName(), new Object[]{lessonRepository.countByCourse(c)}))
                .sorted((c1,c2) -> direction.isAscending() ?  ((int)c1.values()[0] - (int)c2.values()[0]) : ((int)c2.values()[0] - (int)c1.values()[0]))
                .limit(limit)
                .toList();
    }

    @Override
    public long getActiveCoursesCount() {
        return courseRepository.countActiveCourses();
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

//        c.getTasks().forEach(t -> t.setCourse(null));
//        c.getTasks().clear();

        List<Task> courseTasks = new ArrayList<>(c.getTasks().stream().toList());
        List<TaskCourseDTO> updatedTasks = dto.getStructure().getTasks();

        List<Long> oldTaskIds = courseTasks.stream().map(Task::getId).toList();
        List<Long> updatedTaskIds = updatedTasks.stream().map(TaskCourseDTO::getId).toList();

        // ids of old tasks to remove
        courseTasks.stream()
                        .filter(t -> !updatedTaskIds.contains(t.getId()))
                        .forEach(t -> {
                            t.setCourse(null);
                            c.getTasks().remove(t);
                        });

        // ids of new tasks to add
//        updatedTasks.stream()
//                        .filter(t -> !oldTaskIds.contains(t.getId()))
//                        .forEach(t -> {
//                            taskRepository.findById(t.getId()).ifPresent(foundTask -> {
//                                foundTask.setTaskIndex(t.getTaskIndex());
//                                foundTask.setCourse(c);
//                                taskRepository.save(foundTask);
//                                c.getTasks().add(foundTask);
//                            });
//                        });

        updatedTasks.forEach(t -> taskRepository.findById(t.getId()).ifPresent(foundTask -> {
            foundTask.setTaskIndex(t.getTaskIndex());
            foundTask.setCourse(c);
            taskRepository.save(foundTask);
            if(!oldTaskIds.contains(t.getId())) {
                c.getTasks().add(foundTask);
            }
        }));


//        log.info(dto.getStructure().getTasks().toString());


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
        Optional.ofNullable(c.getMentor()).ifPresent(mentor -> mentor.getCourses().remove(c));
        Optional.ofNullable(c.getManager()).ifPresent(manager -> manager.getCourses().remove(c));
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
