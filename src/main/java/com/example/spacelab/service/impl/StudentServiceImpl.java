package com.example.spacelab.service.impl;

import com.example.spacelab.model.Student;
import com.example.spacelab.repository.StudentRepository;
import com.example.spacelab.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public List<Student> getStudents() {
        return null;
    }

    @Override
    public Student getStudentById(Long id) {
        return null;
    }

    @Override
    public Student createNewStudent(Student student) {
        return null;
    }

    @Override
    public Student editStudent(Student student) {
        return null;
    }

    @Override
    public void deleteStudentById(Long id) {

    }
}
