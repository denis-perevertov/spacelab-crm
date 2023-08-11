package com.example.spacelab.service;

import com.example.spacelab.model.Student;

import java.util.List;

public interface StudentService {
    List<Student> getStudents();
    Student getStudentById(Long id);
    Student createNewStudent(Student student);
    Student editStudent(Student student);
    void deleteStudentById(Long id);
}
