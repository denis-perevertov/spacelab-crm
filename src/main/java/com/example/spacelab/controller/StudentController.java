package com.example.spacelab.controller;

import com.example.spacelab.api.StudentAPI;
import com.example.spacelab.dto.SelectDTO;
import com.example.spacelab.dto.course.StudentCourseTaskInfoDTO;
import com.example.spacelab.dto.student.*;
import com.example.spacelab.exception.ErrorMessage;
import com.example.spacelab.exception.ObjectValidationException;
import com.example.spacelab.mapper.CourseMapper;
import com.example.spacelab.mapper.LessonMapper;
import com.example.spacelab.mapper.StudentMapper;
import com.example.spacelab.mapper.TaskMapper;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.lesson.LessonReportRow;
import com.example.spacelab.model.role.PermissionType;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.student.StudentAccountStatus;
import com.example.spacelab.service.CourseService;
import com.example.spacelab.service.LessonReportRowService;
import com.example.spacelab.service.StudentService;
import com.example.spacelab.util.AuthUtil;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.validator.StudentValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
public class StudentController implements StudentAPI {

    private final CourseService courseService;
    private final StudentService studentService;
    private final LessonReportRowService lessonReportRowService;
    private final StudentMapper studentMapper;
    private final StudentValidator studentValidator;
    private final TaskMapper taskMapper;
    private final CourseMapper courseMapper;
    private final LessonMapper lessonMapper;

    private final AuthUtil authUtil;

    @Value("${application.frontend-personal-cabinet.hostname}")
    private String hostname;

    @GetMapping
    @Transactional
    public ResponseEntity<?> getStudents(@Parameter(name = "Filter object", description = "Collection of all filters for search results", example = "{}") FilterForm filters,
                                                        @RequestParam(required = false, defaultValue = "0") Integer page,
                                                        @RequestParam(required = false, defaultValue = "10") Integer size) {
        Page<StudentDTO> students = new PageImpl<>(new ArrayList<>());
        Page<Student> studentPage;
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");

        Admin loggedInAdmin = authUtil.getLoggedInAdmin();
        PermissionType permissionForLoggedInAdmin = loggedInAdmin.getRole().getPermissions().getReadStudents();
        Set<Course> adminCourses = loggedInAdmin.getCourses();

        if(permissionForLoggedInAdmin == PermissionType.FULL) {
            studentPage = studentService.getStudents(filters.trim(), pageable);
            students = new PageImpl<>(studentPage.getContent().stream().map(studentMapper::fromStudentToDTO).toList(), pageable, studentPage.getTotalElements());
        }
        else if(permissionForLoggedInAdmin == PermissionType.PARTIAL) {
            Long[] allowedCoursesIDs = adminCourses.stream().map(Course::getId).toList().toArray(new Long[adminCourses.size()]);
            studentPage = studentService.getStudentsByAllowedCourses(filters.trim(), pageable, allowedCoursesIDs);
            students = new PageImpl<>(studentPage.getContent().stream().map(studentMapper::fromStudentToDTO).toList(), pageable, studentPage.getTotalElements());
        }

        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/{studentID}")
    public ResponseEntity<?> getStudent(@PathVariable @Parameter(example = "1") Long studentID) {

        authUtil.checkAccessToCourse(studentService.getStudentCourseID(studentID), "students.read");

        Student student = studentService.getStudentById(studentID);
        return new ResponseEntity<>(studentMapper.fromStudentToDTO(student), HttpStatus.OK);
    }


    @GetMapping("/{studentID}/card")
    public ResponseEntity<?> getStudentCard(@PathVariable @Parameter(example = "1") Long studentID) {

        authUtil.checkAccessToCourse(studentService.getStudentCourseID(studentID), "students.read");

        StudentCardDTO card = studentService.getCard(studentID);
        return new ResponseEntity<>(card, HttpStatus.OK);
    }

    @GetMapping("/{studentID}/lessons")
    public ResponseEntity<?> getStudentLessons(@PathVariable @Parameter(example = "1") Long studentID) {
        List<LessonReportRow> studentLessonData = studentService.getStudentLessonData(studentID);
        return new ResponseEntity<>(studentLessonData, HttpStatus.OK);
    }

    @GetMapping("/{studentID}/course")
    public ResponseEntity<?> getStudentCourseInfo(@PathVariable Long studentID) {
        StudentCourseTaskInfoDTO studentCourseTaskInfo = courseService.getStudentCourseInfo(studentID);
        return ResponseEntity.ok(studentCourseTaskInfo);
    }

    @GetMapping("/{studentID}/lesson-data")
    public ResponseEntity<?> getStudentLessonData(@PathVariable Long studentID,
                                                  FilterForm filters,
                                                  @RequestParam(required = false, defaultValue = "0") Integer page,
                                                  @RequestParam(required = false, defaultValue = "10") Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");
        filters.setStudent(studentID);
        return ResponseEntity.ok(
                lessonReportRowService.getStudentLessonReports(filters.trim(), pageable)
                        .map(lessonMapper::fromReportRowToDTO)
        );
    }

    @PostMapping
    public ResponseEntity<?> createNewStudent(@RequestBody StudentEditDTO dto,
                                                    BindingResult bindingResult) {

        authUtil.checkAccessToCourse(dto.courseID(), "students.write");
        studentValidator.validate(dto, bindingResult);

        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ObjectValidationException(errors);
        }

        Student student = studentService.createNewStudent(studentMapper.fromEditDTOToStudent(dto));
        return new ResponseEntity<>(studentMapper.fromStudentToDTO(student), HttpStatus.CREATED);

    }

    @PostMapping("/invite")
    public ResponseEntity<?> createStudentInviteLink(@RequestBody @Valid StudentInviteRequestDTO inviteRequest,
                                                          BindingResult bindingResult,
                                                          HttpServletRequest req) {

        log.info(req.getRequestURI());
        log.info(req.getContextPath());
        log.info(req.getRemoteHost());
        log.info(req.getScheme());
        log.info(req.getServerName());
        log.info(String.valueOf(req.getServerPort()));

        authUtil.checkAccessToCourse(inviteRequest.getCourseID(), "students.invite");

        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ObjectValidationException(errors);
        }

        String token = studentService.createInviteStudentToken(studentMapper.fromDTOToInviteRequest(inviteRequest));
        String url = req.getScheme() + "://" + hostname + "/register?invite_key=" + token;
        return new ResponseEntity<>(url, HttpStatus.CREATED);

    }

    @PostMapping("/register")
    public ResponseEntity<?> registerStudent(@RequestBody StudentRegisterDTO dto,
                                                      BindingResult bindingResult) {

        studentValidator.validate(dto, bindingResult);

        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ObjectValidationException(errors);
        }

        Student student = studentService.registerStudent(studentMapper.fromRegisterDTOToStudent(dto));
        return new ResponseEntity<>(studentMapper.fromStudentToDTO(student), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/edit")
    public ResponseEntity<?> loadStudentForEdit(@PathVariable Long id) {
        return new ResponseEntity<>(studentMapper.fromStudentToEditDTO(studentService.getStudentById(id)), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editStudent(@PathVariable @Parameter(example = "1") Long id,
                                                  @RequestBody StudentEditDTO dto,
                                                  BindingResult bindingResult) {

        authUtil.checkAccessToCourse(dto.courseID(), "students.edit");

        StudentEditDTO dtoWithID = new StudentEditDTO(id, dto);

        studentValidator.validate(dtoWithID, bindingResult);

        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ObjectValidationException(errors);
        }


        Student student = studentService.editStudent(studentMapper.fromEditDTOToStudent(dtoWithID));
        return new ResponseEntity<>(studentMapper.fromStudentToDTO(student), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable @Parameter(example = "1") Long id) {

        authUtil.checkAccessToCourse(studentService.getStudentCourseID(id), "students.delete");

        studentService.deleteStudentById(id);
        return new ResponseEntity<>("Student with ID:"+id+" deleted", HttpStatus.OK);
    }

    // =========================================

    @GetMapping("/get-student-avatars")
    public ResponseEntity<?> getStudentAvatars(@RequestParam(required = false, defaultValue = "0") Integer page,
                                                    @RequestParam(required = false, defaultValue = "0") Integer size) {

        Page<Student> studentPage;
        if(page == null) studentPage = new PageImpl<>(studentService.getStudents());
        else if(size == null) studentPage = studentService.getStudents(PageRequest.of(page, 10));
        else studentPage = studentService.getStudents(PageRequest.of(page, size));

        return ResponseEntity.ok(new PageImpl<>(studentPage.getContent().stream().map(studentMapper::fromStudentToAvatarDTO).toList()));
    }

    @GetMapping("/available")
    public ResponseEntity<?> getStudentsWithoutCourse(FilterForm filters,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "course");
        Page<Student> studentPage = studentService.getAvailableStudents(filters.trim(), pageable);

        return ResponseEntity.ok(studentPage.map(student -> new StudentModalDTO(student.getId(),
                student.getFullName(),
                student.getDetails().getAccountStatus().name(),
                student.getDetails().getEmail(),
                student.getAvatar(),
                (student.getCourse() != null) ? student.getCourse().getName() : "")));
    }

    @GetMapping("/get-status-list")
    public ResponseEntity<?> getStatusList() {
        return ResponseEntity.ok(Arrays.stream(StudentAccountStatus.values()).map(v -> new SelectDTO(v.name(), v.name())).toList());
    }


}
