package com.example.spacelab.service.impl;

import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.mapper.StudentMapper;
import com.example.spacelab.mapper.TaskMapper;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.lesson.LessonReportRow;
import com.example.spacelab.model.student.StudentInviteRequest;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.student.StudentTask;
import com.example.spacelab.dto.student.StudentCardDTO;
import com.example.spacelab.repository.*;
import com.example.spacelab.service.StudentService;
import com.example.spacelab.service.specification.StudentSpecifications;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.model.student.StudentAccountStatus;
import com.example.spacelab.model.student.StudentTaskStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Log
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final InviteStudentRequestRepository inviteRepository;
    private final StudentTaskRepository studentTaskRepository;
    private final UserRoleRepository userRoleRepository;

    private final StudentMapper studentMapper;
    private final TaskMapper taskMapper;

    @Override
    public List<Student> getStudents() {
        log.info("Getting all students' info without filters or pages...");
        return studentRepository.findAll();
    }

    @Override
    public Page<Student> getStudents(Pageable pageable) {
        log.info("Getting all students' info with page " + pageable.getPageNumber() +
                " / size " + pageable.getPageSize());
        return studentRepository.findAll(pageable);
    }

    public Page<Student> getStudents(FilterForm filters, Pageable pageable) {
        log.info("Getting all students' info with page " + pageable.getPageNumber() +
                " / size " + pageable.getPageSize() + " and filters: " + filters);
        Specification<Student> spec = buildSpecificationFromFilters(filters);
        return studentRepository.findAll(spec, pageable);
    }

    public List<Student> getStudentsByAllowedCourses(Long... ids) {
        log.info("Getting all students' info without filters or pages | for courses with IDs: " + Arrays.toString(ids));
        return studentRepository.findAllByAllowedCourse(ids);
    }

    public Page<Student> getStudentsByAllowedCourses(Pageable pageable, Long... ids) {
        log.info("Getting all students' info with page " + pageable.getPageNumber() +
                " / size " + pageable.getPageSize() + " | for courses with IDs: " + Arrays.toString(ids));
        return studentRepository.findAllByAllowedCoursePage(pageable, ids);
    }

    public Page<Student> getStudentsByAllowedCourses(FilterForm filters, Pageable pageable, Long... ids) {
        log.info("Getting all students' info with page " + pageable.getPageNumber() +
                " / size " + pageable.getPageSize() + " and filters: " + filters + " | for courses with IDs: " + Arrays.toString(ids));
        Specification<Student> spec = buildSpecificationFromFilters(filters).and(StudentSpecifications.hasCourseIDs(ids));
        return studentRepository.findAll(spec, pageable);
    }

    @Override
    public Student getStudentById(Long id) {
        log.info("Getting student with ID: " + id);
        Student student = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student not found", Student.class));
        return student;
    }

    @Override
    public Student createNewStudent(Student student) {

        student.setRating(0);
        student.setRole(userRoleRepository.getReferenceByName("STUDENT"));
        student.getDetails().setAccountStatus(StudentAccountStatus.ACTIVE);

        student = studentRepository.save(student);
        log.info("Created student: " + student);
        return student;
    }

    @Override
    public Student registerStudent(Student student) {

        student.setRating(0);
        student.setRole(userRoleRepository.getReferenceByName("STUDENT"));
        student.getDetails().setAccountStatus(StudentAccountStatus.ACTIVE);

        student = studentRepository.save(student);
        log.info("Created student: " + student);
        return student;
    }

    @Override
    public Student editStudent(Student student) {

        student = studentRepository.save(student);
        log.info("Edited student: " + student);
        return student;
    }

    @Override
    public StudentCardDTO getCard(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student not found", Student.class));
        return studentMapper.fromStudentToCardDTO(student);
    }

    @Override
    public void deleteStudentById(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student not found", Student.class));
        log.info("Deleting student with ID: " + id);
        studentRepository.delete(student);
    }


    /*
     = = = = = Задания студента = = = = =
     */


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
    public StudentTask getStudentTask(Long taskID) {
        log.info("Getting student task with taskID: " + taskID);
        StudentTask task = studentTaskRepository.findById(taskID).orElseThrow(() -> new ResourceNotFoundException("Student task not found", StudentTask.class));
        return task;
    }

    @Override
    public String createInviteStudentToken(StudentInviteRequest request) {
        log.info("Creating token to use in Student invite URL...");
        UUID id = UUID.randomUUID();
        request.setId(id.toString());
        log.info("Created token: " + id + ", saving token with set parameters in DB");
        request = inviteRepository.save(request);
        return request.getId();
    }

    @Override
    public List<LessonReportRow> getStudentLessonData(Long studentID) {
        return getStudentById(studentID).getLessonData();
    }

    @Override
    public Long getStudentCourseID(Long studentID) {
        return getStudentById(studentID).getCourse().getId();
    }

    @Override
    public Specification<Student> buildSpecificationFromFilters(FilterForm filters) {

        log.info("Building specification from filters: " + filters);
        
        String nameEmailInput = filters.getName();
        Long courseID = filters.getCourse();
        String phoneInput = filters.getPhone();
        String telegramInput = filters.getTelegram();
        Integer ratingInput = filters.getRating();
        String statusInput = filters.getStatus();

        Course course = (courseID == null) ? null : courseRepository.getReferenceById(courseID);
        StudentAccountStatus status = (statusInput == null) ? null : StudentAccountStatus.valueOf(statusInput);

        Specification<Student> spec = Specification.where(StudentSpecifications.hasNameOrEmailLike(nameEmailInput)
                                                    .and(StudentSpecifications.hasCourse(course))
                                                    .and(StudentSpecifications.hasPhoneLike(phoneInput))
                                                    .and(StudentSpecifications.hasTelegramLike(telegramInput))
                                                    .and(StudentSpecifications.hasRatingOrHigher(ratingInput))
                                                    .and(StudentSpecifications.hasStatus(status)));

        return spec;
    }
}
