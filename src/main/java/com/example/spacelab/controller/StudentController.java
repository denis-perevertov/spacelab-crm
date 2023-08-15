package com.example.spacelab.controller;

import com.example.spacelab.model.Student;
import com.example.spacelab.model.dto.StudentDTO;
import com.example.spacelab.model.dto.TaskDTO;
import com.example.spacelab.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
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
    public List<StudentDTO> getStudents() {
        return studentService.getStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudent(@PathVariable Long id) {
        return new ResponseEntity<>(studentService.getStudentDTOById(id), HttpStatus.FOUND);
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<TaskDTO>> getStudentTasks(@PathVariable Long id) {
        return new ResponseEntity<>(studentService.getStudentTasks(id), HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<StudentDTO> createNewStudent(@RequestBody StudentDTO student) {
        return new ResponseEntity<>(studentService.createNewStudent(student), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> editStudent(@PathVariable Long id, @RequestBody StudentDTO student) {
        return new ResponseEntity<>(studentService.editStudent(student), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudentById(id);
        return new ResponseEntity<>("Student with ID:"+id+" deleted", HttpStatus.OK);
    }
}
