package com.example.spacelab.controller;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@Log
@RequiredArgsConstructor
@RequestMapping("/api/students")
public class StudentController {

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

    // Получение студентов (с фильтрами/страницами)
    @Operation(description = "Get list of students paginated by 'page/size' params (default values are 0/10), output depends on permission type(full/partial)", summary = "Get Students", tags = {"Student"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('students.read.NO_ACCESS')")
    @GetMapping
    @Transactional
    public ResponseEntity<Page<StudentDTO>> getStudents(@Parameter(name = "Filter object", description = "Collection of all filters for search results", example = "{}") FilterForm filters,
                                                        @RequestParam(required = false, defaultValue = "0") Integer page,
                                                        @RequestParam(required = false, defaultValue = "10") Integer size) {
        Page<StudentDTO> students = new PageImpl<>(new ArrayList<>());
        Page<Student> studentPage;
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");

        Admin loggedInAdmin = authUtil.getLoggedInAdmin();
        PermissionType permissionForLoggedInAdmin = loggedInAdmin.getRole().getPermissions().getReadStudents();
        Set<Course> adminCourses = loggedInAdmin.getCourses();

        if(permissionForLoggedInAdmin == PermissionType.FULL) {
            studentPage = studentService.getStudents(filters, pageable);
            students = new PageImpl<>(studentPage.getContent().stream().map(studentMapper::fromStudentToDTO).toList(), pageable, studentPage.getTotalElements());
        }
        else if(permissionForLoggedInAdmin == PermissionType.PARTIAL) {
            Long[] allowedCoursesIDs = adminCourses.stream().map(Course::getId).toList().toArray(new Long[adminCourses.size()]);
            studentPage = studentService.getStudentsByAllowedCourses(filters, pageable, allowedCoursesIDs);
            students = new PageImpl<>(studentPage.getContent().stream().map(studentMapper::fromStudentToDTO).toList(), pageable, studentPage.getTotalElements());
        }

        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    // Получение одного студента
    @Operation(description = "Get student DTO by its ID", summary = "Get Student", tags = {"Student"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Student not found in DB", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('students.read.NO_ACCESS')")
    @GetMapping("/{studentID}")
    public ResponseEntity<StudentDTO> getStudent(@PathVariable @Parameter(example = "1") Long studentID) {

        authUtil.checkAccessToCourse(studentService.getStudentCourseID(studentID), "students.read");

        Student student = studentService.getStudentById(studentID);
        return new ResponseEntity<>(studentMapper.fromStudentToDTO(student), HttpStatus.OK);
    }


    // Получение карточки информации о студенте
    @Operation(description = "Get student card DTO by his ID", summary = "Get Student Info Card", tags = {"Student"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Student not found in DB", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('students.read.NO_ACCESS')")
    @GetMapping("/{studentID}/card")
    public ResponseEntity<StudentCardDTO> getStudentCard(@PathVariable @Parameter(example = "1") Long studentID) {

        authUtil.checkAccessToCourse(studentService.getStudentCourseID(studentID), "students.read");

        StudentCardDTO card = studentService.getCard(studentID);
        return new ResponseEntity<>(card, HttpStatus.OK);
    }

    // Получение списка занятий студента
    @Operation(description = "Get all lesson data associated with a student, by his ID", summary = "Get Student Lesson Data", tags = {"Student Lesson"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Student not found in DB", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('students.read.NO_ACCESS')")
    @GetMapping("/{studentID}/lessons")
    public ResponseEntity<List<LessonReportRow>> getStudentLessons(@PathVariable @Parameter(example = "1") Long studentID) {
        List<LessonReportRow> studentLessonData = studentService.getStudentLessonData(studentID);
        return new ResponseEntity<>(studentLessonData, HttpStatus.OK);
    }

    // получить имя, иконку и информацию текущего курса студента
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
                lessonReportRowService.getStudentLessonReports(filters, pageable)
                        .map(lessonMapper::fromReportRowToDTO)
        );
    }

    // Создание нового студента (не регистрация)
    @Operation(description = "Create new student (create manually, not register) - helper method, might remove later", summary = "Create New Student", tags = {"Student"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created"),
            @ApiResponse(responseCode = "400", description = "Bad Request / Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('students.write.NO_ACCESS')")
    @PostMapping
    public ResponseEntity<StudentDTO> createNewStudent(@RequestBody StudentEditDTO dto,
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

    // Формирование ссылки на приглашение студента
    @Operation(description = "Create new link that students can use to register in the application", summary = "Create New Register Link For Students", tags = {"Student"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('students.invite.NO_ACCESS')")
    @PostMapping("/invite")
    public ResponseEntity<String> createStudentInviteLink(@RequestBody StudentInviteRequestDTO inviteRequest,
                                                          HttpServletRequest servletRequest) {

        authUtil.checkAccessToCourse(inviteRequest.getCourseID(), "students.invite");

        String token = studentService.createInviteStudentToken(studentMapper.fromDTOToInviteRequest(inviteRequest));
        String url = "http://" + hostname + "/register?invite_key=" + token;
        return new ResponseEntity<>(url, HttpStatus.CREATED);

    }

    // Регистрация студента ; для захода сюда защита не нужна
    @Operation(description = "Register new student (register by using the created link)", summary = "Register New Student", tags = {"Student"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created"),
            @ApiResponse(responseCode = "400", description = "Bad Request / Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<StudentDTO> registerStudent(@RequestBody StudentRegisterDTO dto,
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

    // Загрузка студента для редактирования
    @GetMapping("/{id}/edit")
    public ResponseEntity<StudentEditDTO> loadStudentForEdit(@PathVariable Long id) {
        return new ResponseEntity<>(studentMapper.fromStudentToEditDTO(studentService.getStudentById(id)), HttpStatus.OK);
    }

    // Редактирование студента
    @Operation(description = "Update existing student in the application; ID field does not matter in write/edit operations", summary = "Update Role", tags = {"Student"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "400", description = "Bad Request / Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('students.edit.NO_ACCESS')")
    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> editStudent(@PathVariable @Parameter(example = "1") Long id,
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

    // Удаление студента
    @Operation(description = "Delete student by his ID", summary = "Delete Student", tags = {"Student"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Student not found in DB", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('students.delete.NO_ACCESS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable @Parameter(example = "1") Long id) {

        authUtil.checkAccessToCourse(studentService.getStudentCourseID(id), "students.delete");

        studentService.deleteStudentById(id);
        return new ResponseEntity<>("Student with ID:"+id+" deleted", HttpStatus.OK);
    }

    // =========================================

    // Получение списка аватарок студентов
    @Operation(description = "Get the list of Students' ID, Name and Avatar picture", summary = "Get Students Avatar List", tags = {"Student"})
    @GetMapping("/get-student-avatars")
    public Page<StudentAvatarDTO> getStudentAvatars(@RequestParam(required = false, defaultValue = "0") Integer page,
                                                    @RequestParam(required = false, defaultValue = "0") Integer size) {

        Page<Student> studentPage;
        if(page == null) studentPage = new PageImpl<>(studentService.getStudents());
        else if(size == null) studentPage = studentService.getStudents(PageRequest.of(page, 10));
        else studentPage = studentService.getStudents(PageRequest.of(page, size));

        return new PageImpl<>(studentPage.getContent().stream().map(studentMapper::fromStudentToAvatarDTO).toList());
    }

    // Получение списка незанятых студентов (студентов без курса)
    @Operation(description = "Get the list of students who aren't enrolled in any course", summary = "Get Students Without Courses", tags = {"Student"})
    @GetMapping("/available")
    public Page<StudentModalDTO> getStudentsWithoutCourse(FilterForm filters,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "course");
        Page<Student> studentPage = studentService.getAvailableStudents(filters, pageable);

        return studentPage.map(student -> new StudentModalDTO(student.getId(),
                student.getFullName(),
                student.getDetails().getAccountStatus().name(),
                student.getDetails().getEmail(),
                student.getAvatar(),
                (student.getCourse() != null) ? student.getCourse().getName() : ""));
    }

    // Получение списка статусов студентов
    @GetMapping("/get-status-list")
    public ResponseEntity<?> getStatusList() {
        return ResponseEntity.ok(Arrays.stream(StudentAccountStatus.values()).map(v -> new SelectDTO(v.name(), v.name())).toList());
    }


}
