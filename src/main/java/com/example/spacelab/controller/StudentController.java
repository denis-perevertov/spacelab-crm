package com.example.spacelab.controller;

import com.example.spacelab.model.Student;
import com.example.spacelab.model.dto.TaskDTO;
import com.example.spacelab.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
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
    public List<Student> getStudents() {
        return studentService.getStudents();
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @GetMapping("/{id}/tasks")
    public List<TaskDTO> getStudentTasks(@PathVariable Long id) {
        return studentService.getStudentTasks(id);
    }

    @PostMapping
    public Student createNewStudent(@RequestBody Student student) {
        return studentService.createNewStudent(student);
    }

    @PutMapping("/{id}")
    public Student editStudent(@RequestBody Student student) {
        return studentService.editStudent(student);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudentById(id);
        return new ResponseEntity<>("Student deleted", HttpStatus.OK);
    }
}
