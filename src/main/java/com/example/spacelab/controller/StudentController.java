package com.example.spacelab.controller;

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
import com.example.spacelab.validator.ValidationErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Log
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;
    private final StudentValidator studentValidator;
    private final TaskMapper taskMapper;

    // Получение студентов (с фильтрами/страницами)
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
    @GetMapping("/{studentID}")
    public ResponseEntity<StudentDTO> getStudent(@PathVariable Long studentID) {
        Student student = studentService.getStudentById(studentID);
        return new ResponseEntity<>(studentMapper.fromStudentToDTO(student), HttpStatus.OK);
    }

    // Получение заданий одного студента
    @GetMapping("/{studentID}/tasks")
    public ResponseEntity<List<StudentTaskDTO>> getStudentTasks(@PathVariable Long studentID,
                                                                @RequestParam(required = false) StudentTaskStatus status) {
        List<StudentTaskDTO> taskList;
        if(status == null) taskList = studentService.getStudentTasks(studentID).stream().map(taskMapper::fromStudentTaskToDTO).toList();
        else taskList = studentService.getStudentTasks(studentID, status).stream().map(taskMapper::fromStudentTaskToDTO).toList();

        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }


    // Получение одного задания одного студента
    @GetMapping("/{studentID}/tasks/{taskID}")
    public ResponseEntity<StudentTaskDTO> getStudentTask(@PathVariable Long studentID,
                                                         @PathVariable Long taskID) {
        StudentTaskDTO task = taskMapper.fromStudentTaskToDTO(studentService.getStudentTask(taskID));
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    // Получение карточки информации о студенте
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
    @PostMapping
    public ResponseEntity<?> createNewStudent(@RequestBody StudentEditDTO dto,
                                               BindingResult bindingResult) {

        studentValidator.validate(dto, bindingResult);

        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return new ResponseEntity<>(new ValidationErrorMessage(HttpStatus.BAD_REQUEST.value(), errors), HttpStatus.BAD_REQUEST);
        }

        Student student = studentService.createNewStudent(studentMapper.fromEditDTOToStudent(dto));
        return new ResponseEntity<>(studentMapper.fromStudentToDTO(student), HttpStatus.CREATED);

    }

    // Формирование ссылки на приглашение студента
    @PostMapping("/invite")
    public ResponseEntity<String> createStudentInviteLink(@AuthenticationPrincipal Admin admin,
                                                          @RequestBody StudentInviteRequest inviteRequest,
                                                          HttpServletRequest servletRequest) {

        String token = studentService.createInviteStudentToken(inviteRequest);
        String url = "http://" + servletRequest.getServerName() + ":" + servletRequest.getServerPort() + "/register/" + token;
        return new ResponseEntity<>(url, HttpStatus.CREATED);

    }

    // Регистрация студента
    @PostMapping("/register")
    public ResponseEntity<?> registerStudent(@RequestBody StudentRegisterDTO dto,
                                                      BindingResult bindingResult) {

        studentValidator.validate(dto, bindingResult);

        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return new ResponseEntity<>(new ValidationErrorMessage(HttpStatus.BAD_REQUEST.value(), errors), HttpStatus.BAD_REQUEST);
        }

        Student student = studentService.registerStudent(studentMapper.fromRegisterDTOToStudent(dto));
        return new ResponseEntity<>(studentMapper.fromStudentToDTO(student), HttpStatus.CREATED);
    }

    // Редактирование студента
    @PutMapping("/{id}")
    public ResponseEntity<?> editStudent(@PathVariable Long id,
                                          @RequestBody StudentEditDTO dto,
                                          BindingResult bindingResult) {
        StudentEditDTO dtoWithID = new StudentEditDTO(id, dto);

        studentValidator.validate(dtoWithID, bindingResult);

        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return new ResponseEntity<>(new ValidationErrorMessage(HttpStatus.BAD_REQUEST.value(), errors), HttpStatus.BAD_REQUEST);
        }

        Student student = studentService.editStudent(studentMapper.fromEditDTOToStudent(dtoWithID));
        return new ResponseEntity<>(studentMapper.fromStudentToDTO(student), HttpStatus.OK);
    }

    // Удаление студента
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
