package com.example.spacelab.controller;

import com.example.spacelab.model.InviteStudentRequest;
import com.example.spacelab.model.LessonReportRow;
import com.example.spacelab.model.Student;
import com.example.spacelab.model.dto.StudentDTO;
import com.example.spacelab.model.dto.StudentTaskDTO;
import com.example.spacelab.model.dto.TaskDTO;
import com.example.spacelab.service.StudentService;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.util.StudentTaskStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Log
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<?> getStudents(FilterForm filters,
                                         @RequestParam(required = false) Integer page,
                                         @RequestParam(required = false) Integer size) {

        System.out.println(filters.toString());

        if(page == null) return ResponseEntity.badRequest().body("Page is not specified");
        else if(size == null) return ResponseEntity.badRequest().body("Size is not specified");

        return new ResponseEntity<>(studentService.getStudents(filters, PageRequest.of(page, size)), HttpStatus.OK);
    }

    @GetMapping("/{studentID}")
    public ResponseEntity<StudentDTO> getStudent(@PathVariable Long studentID) {
        return new ResponseEntity<>(studentService.getStudentDTOById(studentID), HttpStatus.OK);
    }

    @GetMapping("/{studentID}/tasks")
    public ResponseEntity<List<StudentTaskDTO>> getStudentTasks(@PathVariable Long studentID,
                                                                @RequestParam(required = false) StudentTaskStatus status) {
        List<StudentTaskDTO> taskList;
        if(status == null) taskList = studentService.getStudentTasks(studentID);
        else taskList = studentService.getStudentTasks(studentID, status);

        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }

    @GetMapping("/{studentID}/tasks/{taskID}")
    public ResponseEntity<StudentTaskDTO> getStudentTask(@PathVariable Long studentID,
                                                         @PathVariable Long taskID) {
        StudentTaskDTO task = studentService.getStudentTask(taskID);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    /*

        TODO
        занятия

    @GetMapping("/{studentID}/lessons")
    public ResponseEntity<LessonReportRow> getStudentLessons(@PathVariable Long studentID) {

        return new ResponseEntity<>();
    }

    */

    @PostMapping
    public ResponseEntity<StudentDTO> createNewStudent(@Valid @RequestBody StudentDTO student) {
        return new ResponseEntity<>(studentService.createNewStudent(student), HttpStatus.CREATED);
    }

    @PostMapping("/invite")
    public ResponseEntity<String> createStudentInviteLink(@RequestBody InviteStudentRequest inviteRequest,
                                                          HttpServletRequest servletRequest) {
        String token = studentService.createInviteStudentToken(inviteRequest);
        String url = "http://" + servletRequest.getServerName() + ":" + servletRequest.getServerPort() + "/register/" + token;
        return new ResponseEntity<>(url, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> editStudent(@PathVariable Long id,
                                                  @Valid @RequestBody StudentDTO student) {
        return new ResponseEntity<>(studentService.editStudent(student), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudentById(id);
        return new ResponseEntity<>("Student with ID:"+id+" deleted", HttpStatus.OK);
    }
}
