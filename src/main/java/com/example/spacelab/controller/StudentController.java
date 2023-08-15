package com.example.spacelab.controller;

import com.example.spacelab.model.Student;
import com.example.spacelab.model.dto.StudentDTO;
import com.example.spacelab.model.dto.TaskDTO;
import com.example.spacelab.service.StudentService;
import com.example.spacelab.util.FilterForm;
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

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudent(@PathVariable Long id) {
        return new ResponseEntity<>(studentService.getStudentDTOById(id), HttpStatus.OK);
    }
//
//    @GetMapping("/{id}/tasks")
//    public ResponseEntity<List<TaskDTO>> getStudentTasks(@PathVariable Long id) {
//        return new ResponseEntity<>(studentService.getStudentTasks(id), HttpStatus.FOUND);
//    }

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
