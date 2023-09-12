package com.example.spacelab.controller;

import com.example.spacelab.dto.SelectSearchDTO;
import com.example.spacelab.dto.student.StudentTaskDTO;
import com.example.spacelab.dto.student.*;
import com.example.spacelab.exception.ErrorMessage;
import com.example.spacelab.exception.ObjectValidationException;
import com.example.spacelab.mapper.StudentMapper;
import com.example.spacelab.mapper.TaskMapper;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.lesson.LessonReportRow;
import com.example.spacelab.model.role.PermissionType;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.student.StudentInviteRequest;
import com.example.spacelab.model.student.StudentTaskStatus;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@Log
@RequiredArgsConstructor
@RequestMapping("/api/students")
public class StudentController {


    private final StudentService studentService;
    private final StudentMapper studentMapper;
    private final StudentValidator studentValidator;
    private final TaskMapper taskMapper;

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
    public ResponseEntity<Page<StudentDTO>> getStudents(@Parameter(name = "Filter object", description = "Collection of all filters for search results", example = "{}") FilterForm filters,
                                                        @RequestParam(required = false, defaultValue = "0") Integer page,
                                                        @RequestParam(required = false, defaultValue = "10") Integer size) {

        Page<StudentDTO> students = new PageImpl<>(new ArrayList<>());

        Admin loggedInAdmin = AuthUtil.getLoggedInAdmin();
        PermissionType permissionForLoggedInAdmin = loggedInAdmin.getRole().getPermissions().getReadStudents();

        if(permissionForLoggedInAdmin == PermissionType.FULL) {
            if(page == null && size == null) students = new PageImpl<>(studentService.getStudents().stream().map(studentMapper::fromStudentToDTO).toList());
            else if(page != null && size == null) students = new PageImpl<>(studentService.getStudents(filters, PageRequest.of(page, 10)).stream().map(studentMapper::fromStudentToDTO).toList());
            else students = new PageImpl<>(studentService.getStudents(filters, PageRequest.of(page, size)).stream().map(studentMapper::fromStudentToDTO).toList());
        }
        else if(permissionForLoggedInAdmin == PermissionType.PARTIAL) {

            Long[] allowedCoursesIDs = (Long[]) loggedInAdmin.getCourses().stream().map(Course::getId).toArray();

            if(page == null && size == null) students = new PageImpl<>(studentService.getStudentsByAllowedCourses(allowedCoursesIDs).stream().map(studentMapper::fromStudentToDTO).toList());
            else if(page != null && size == null) students = new PageImpl<>(studentService.getStudentsByAllowedCourses(filters, PageRequest.of(page, 10),allowedCoursesIDs).stream().map(studentMapper::fromStudentToDTO).toList());
            else students = new PageImpl<>(studentService.getStudentsByAllowedCourses(filters, PageRequest.of(page, size), allowedCoursesIDs).stream().map(studentMapper::fromStudentToDTO).toList());
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

        AuthUtil.checkAccessToCourse(studentService.getStudentCourseID(studentID), "students.read");

        Student student = studentService.getStudentById(studentID);
        return new ResponseEntity<>(studentMapper.fromStudentToDTO(student), HttpStatus.OK);
    }

    // Получение заданий одного студента
    @Operation(description = "Get student tasks DTO list by student's ID", summary = "Get Student Tasks List", tags = {"Student Task"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Student not found in DB", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('students.read.NO_ACCESS')")
    @GetMapping("/{studentID}/tasks")
    public ResponseEntity<List<StudentTaskDTO>> getStudentTasks(@PathVariable @Parameter(example = "1") Long studentID,
                                                                @RequestParam(required = false) StudentTaskStatus status) {

        AuthUtil.checkAccessToCourse(studentService.getStudentCourseID(studentID), "students.read");

        List<StudentTaskDTO> taskList;
        if(status == null) taskList = studentService.getStudentTasks(studentID).stream().map(taskMapper::fromStudentTaskToDTO).toList();
        else taskList = studentService.getStudentTasks(studentID, status).stream().map(taskMapper::fromStudentTaskToDTO).toList();

        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }


    // Получение одного задания одного студента
    @Operation(description = "Get single student task DTO by student's ID", summary = "Get Student Task", tags = {"Student Task"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentTaskDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Student/task not found in DB", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('students.read.NO_ACCESS')")
    @GetMapping("/{studentID}/tasks/{taskID}")
    public ResponseEntity<StudentTaskDTO> getStudentTask(@PathVariable @Parameter(example = "1") Long studentID,
                                                         @PathVariable @Parameter(example = "1") Long taskID) {

        AuthUtil.checkAccessToCourse(studentService.getStudentCourseID(studentID), "students.read");

        StudentTaskDTO task = taskMapper.fromStudentTaskToDTO(studentService.getStudentTask(taskID));

        return new ResponseEntity<>(task, HttpStatus.OK);
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

        AuthUtil.checkAccessToCourse(studentService.getStudentCourseID(studentID), "students.read");

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

        AuthUtil.checkAccessToCourse(dto.courseID(), "students.write");
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

        AuthUtil.checkAccessToCourse(inviteRequest.getCourseID(), "students.invite");

        String token = studentService.createInviteStudentToken(studentMapper.fromDTOToInviteRequest(inviteRequest));
        String url = "http://" + servletRequest.getServerName() + ":" + servletRequest.getServerPort() + "/register/" + token;
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

        AuthUtil.checkAccessToCourse(dto.courseID(), "students.edit");

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

        AuthUtil.checkAccessToCourse(studentService.getStudentCourseID(id), "students.delete");

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
    @GetMapping("/get-available-students")
    public List<SelectSearchDTO> getStudentsWithoutCourse() {
        return studentService.getStudents().stream().filter(student -> student.getCourse() == null)
                .map(student -> new SelectSearchDTO(student.getId(), student.getFullName())).toList();
    }


}
