package com.example.spacelab.controller;

import com.example.spacelab.dto.role.UserRoleDTO;
import com.example.spacelab.exception.ErrorMessage;
import com.example.spacelab.exception.ObjectValidationException;
import com.example.spacelab.mapper.StudentMapper;
import com.example.spacelab.mapper.TaskMapper;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.dto.student.StudentCardDTO;
import com.example.spacelab.dto.student.StudentDTO;
import com.example.spacelab.dto.student.StudentTaskDTO;
import com.example.spacelab.dto.student.StudentEditDTO;
import com.example.spacelab.dto.student.StudentRegisterDTO;
import com.example.spacelab.model.role.PermissionType;
import com.example.spacelab.model.student.StudentInviteRequest;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.service.StudentService;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.model.student.StudentTaskStatus;
import com.example.spacelab.validator.StudentValidator;
import com.example.spacelab.exception.ValidationErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    @Operation(description = "Get students page", summary = "Get Students", tags = {"Student"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('students.read.NO_ACCESS')")
    @GetMapping
    public ResponseEntity<?> getStudents(FilterForm filters,
                                         @RequestParam(required = false) Integer page,
                                         @RequestParam(required = false) Integer size) {

        Page<StudentDTO> students;

        if(page == null && size == null) students = new PageImpl<>(studentService.getStudents().stream().map(studentMapper::fromStudentToDTO).toList());
        else if(page != null && size == null) students = new PageImpl<>(studentService.getStudents(filters, PageRequest.of(page, 10)).stream().map(studentMapper::fromStudentToDTO).toList());
        else if(page == null) return ResponseEntity.badRequest().body("Size parameter present without page");
        else students = new PageImpl<>(studentService.getStudents(filters, PageRequest.of(page, size)).stream().map(studentMapper::fromStudentToDTO).toList());

        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    // Получение одного студента
    @Operation(description = "Get student DTO by its ID", summary = "Get Student", tags = {"Student"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Student not found in DB", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('students.read.NO_ACCESS')")
    @GetMapping("/{studentID}")
    public ResponseEntity<StudentDTO> getStudent(@PathVariable Long studentID) {
        Student student = studentService.getStudentById(studentID);
        return new ResponseEntity<>(studentMapper.fromStudentToDTO(student), HttpStatus.OK);
    }

    // Получение заданий одного студента
    @Operation(description = "Get student tasks DTO list by student's ID", summary = "Get Student Tasks List", tags = {"Student Task"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Student not found in DB", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('students.read.NO_ACCESS')")
    @GetMapping("/{studentID}/tasks")
    public ResponseEntity<List<StudentTaskDTO>> getStudentTasks(@PathVariable Long studentID,
                                                                @RequestParam(required = false) StudentTaskStatus status) {
        List<StudentTaskDTO> taskList;
        if(status == null) taskList = studentService.getStudentTasks(studentID).stream().map(taskMapper::fromStudentTaskToDTO).toList();
        else taskList = studentService.getStudentTasks(studentID, status).stream().map(taskMapper::fromStudentTaskToDTO).toList();

        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }


    // Получение одного задания одного студента
    @Operation(description = "Get single student task DTO by student's ID", summary = "Get Student Task", tags = {"Student Task"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentTaskDTO.class))),
            @ApiResponse(responseCode = "404", description = "Student/task not found in DB", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('students.read.NO_ACCESS')")
    @GetMapping("/{studentID}/tasks/{taskID}")
    public ResponseEntity<StudentTaskDTO> getStudentTask(@PathVariable Long studentID,
                                                         @PathVariable Long taskID) {
        StudentTaskDTO task = taskMapper.fromStudentTaskToDTO(studentService.getStudentTask(taskID));
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    // Получение карточки информации о студенте
    @Operation(description = "Get student card DTO by his ID", summary = "Get Student Info Card", tags = {"Student"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentCardDTO.class))),
            @ApiResponse(responseCode = "404", description = "Student not found in DB", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('students.read.NO_ACCESS')")
    @GetMapping("/{studentID}/card")
    public ResponseEntity<StudentCardDTO> getStudentCard(@PathVariable Long studentID) {
        StudentCardDTO card = studentService.getCard(studentID);
        return new ResponseEntity<>(card, HttpStatus.OK);
    }

    /*

        TODO
        занятия

    @GetMapping("/{studentID}/lessons")
    public ResponseEntity<LessonReportRow> getStudentLessons(@PathVariable Long studentID) {

        return new ResponseEntity<>();
    }

    */


    // Создание нового студента (не регистрация)
    @Operation(description = "Create new student (create manually, not register)", summary = "Create New Student", tags = {"Student"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('students.write.NO_ACCESS')")
    @PostMapping
    public ResponseEntity<StudentDTO> createNewStudent(@RequestBody StudentEditDTO dto,
                                                    BindingResult bindingResult) {

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
            @ApiResponse(responseCode = "201", description = "Successfully created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('students.invite.NO_ACCESS')")
    @PostMapping("/invite")
    public ResponseEntity<String> createStudentInviteLink(@AuthenticationPrincipal Admin admin,
                                                          @RequestBody StudentInviteRequest inviteRequest,
                                                          HttpServletRequest servletRequest) {

        String token = studentService.createInviteStudentToken(inviteRequest);
        String url = "http://" + servletRequest.getServerName() + ":" + servletRequest.getServerPort() + "/register/" + token;
        return new ResponseEntity<>(url, HttpStatus.CREATED);

    }

    // Регистрация студента ; для захода сюда защита не нужна
    @Operation(description = "Register new student (register by using the created link)", summary = "Register New Student", tags = {"Student"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
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
    @Operation(description = "Update existing student in the application", summary = "Update Role", tags = {"Student"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('students.edit.NO_ACCESS')")
    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> editStudent(@PathVariable Long id,
                                          @RequestBody StudentEditDTO dto,
                                          BindingResult bindingResult) {
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
            @ApiResponse(responseCode = "200", description = "Successfully deleted", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Student not found in DB", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Some unknown error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PreAuthorize("!hasAuthority('students.delete.NO_ACCESS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudentById(id);
        return new ResponseEntity<>("Student with ID:"+id+" deleted", HttpStatus.OK);
    }

    // Проверка доступа
    private void checkAccess(Long courseID, String courseName, Admin admin, PermissionType permissionType) {

        if(permissionType == PermissionType.NO_ACCESS) throw new AccessDeniedException("No access to this operation!");
        else if(permissionType == PermissionType.PARTIAL) {
            if(!admin.getCourses().stream().map(Course::getId).toList().contains(courseID))
                throw new AccessDeniedException("No access to creating new students for course "+courseName+"!");
        }
    }

}
