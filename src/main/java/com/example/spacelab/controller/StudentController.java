package com.example.spacelab.controller;

import com.example.spacelab.mapper.StudentMapper;
import com.example.spacelab.mapper.TaskMapper;
import com.example.spacelab.model.*;
import com.example.spacelab.model.dto.student.StudentCardDTO;
import com.example.spacelab.model.dto.student.StudentDTO;
import com.example.spacelab.model.dto.StudentTaskDTO;
import com.example.spacelab.model.dto.student.StudentRegisterDTO;
import com.example.spacelab.model.role.PermissionType;
import com.example.spacelab.service.StudentService;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.util.StudentTaskStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Log
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;
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
    public ResponseEntity<StudentDTO> createNewStudent(@Valid @RequestBody StudentDTO dto) {

//        Admin admin = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        PermissionType permissionType = admin.getRole().getPermissions().getWriteStudents();
//
//        if(permissionType == PermissionType.NO_ACCESS) throw new AccessDeniedException("No access to creating new students!");
//        else if(permissionType == PermissionType.PARTIAL) {
//            Long studentCourseID = student.getCourse().getId();
//            if(!admin.getCourses().stream().map(Course::getId).toList().contains(studentCourseID))
//                throw new AccessDeniedException("No access to creating new students for course "+student.getCourse().getName()+"!");
//            else return new ResponseEntity<>(studentService.createNewStudent(student), HttpStatus.CREATED);
//        }
//        else return new ResponseEntity<>(studentService.createNewStudent(student), HttpStatus.CREATED);

        Student student = studentService.createNewStudent(studentMapper.fromDTOToStudent(dto));
        return new ResponseEntity<>(studentMapper.fromStudentToDTO(student), HttpStatus.CREATED);

    }

    // Формирование ссылки на приглашение студента
    @PostMapping("/invite")
    public ResponseEntity<String> createStudentInviteLink(@AuthenticationPrincipal Admin admin,
                                                          @RequestBody InviteStudentRequest inviteRequest,
                                                          HttpServletRequest servletRequest) {

        String token = studentService.createInviteStudentToken(inviteRequest);
        String url = "http://" + servletRequest.getServerName() + ":" + servletRequest.getServerPort() + "/register/" + token;
        return new ResponseEntity<>(url, HttpStatus.CREATED);

    }

    // Регистрация студента
    @PostMapping("/register")
    public ResponseEntity<StudentDTO> registerStudent(@Valid @RequestBody StudentRegisterDTO dto) {
        Student student = studentService.registerStudent(studentMapper.fromRegisterDTOToStudent(dto));
        return new ResponseEntity<>(studentMapper.fromStudentToDTO(student), HttpStatus.CREATED);
    }

    /*

    @PostMapping("/invite")
    public ResponseEntity<String> createStudentInviteLink(@AuthenticationPrincipal Admin admin,
                                                          @RequestBody InviteStudentRequest inviteRequest,
                                                          HttpServletRequest servletRequest) {

        checkAccess(inviteRequest.getCourse().getId(),
                inviteRequest.getCourse().getName(),
                admin,
                admin.getRole().getPermissions().getWriteStudents());

        String token = studentService.createInviteStudentToken(inviteRequest);
        String url = "http://" + servletRequest.getServerName() + ":" + servletRequest.getServerPort() + "/register/" + token;
        return new ResponseEntity<>(url, HttpStatus.CREATED);

    }

    */

    // Редактирование студента
    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> editStudent(@PathVariable Long id,
                                                  @Valid @RequestBody StudentDTO dto) {
        dto.setId(id);
        Student student = studentService.editStudent(studentMapper.fromDTOToStudent(dto));
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
