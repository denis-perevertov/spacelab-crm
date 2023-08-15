package com.example.spacelab.service.impl;

import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.mapper.StudentDTOMapper;
import com.example.spacelab.mapper.TaskDTOMapper;
import com.example.spacelab.model.Student;
import com.example.spacelab.model.dto.StudentDTO;
import com.example.spacelab.model.dto.TaskDTO;
import com.example.spacelab.repository.StudentRepository;
import com.example.spacelab.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final StudentDTOMapper studentMapper;
    private final TaskDTOMapper taskMapper;

    @Override
    public List<StudentDTO> getStudents() {
        return studentRepository.findAll().stream().map(studentMapper::fromStudentToDTO).toList();
    }

    @Override
    public List<StudentDTO> getStudentsByPage(Pageable pageable) {
        return studentRepository.findAll(pageable).get().map(studentMapper::fromStudentToDTO).toList();
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student not found"));
    }

    @Override
    public StudentDTO getStudentDTOById(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return studentMapper.fromStudentToDTO(student);
    }
    @Override
    public StudentDTO createNewStudent(StudentDTO dto) {
        Student student = studentMapper.fromDTOToStudent(dto);
        student = studentRepository.save(student);
        return studentMapper.fromStudentToDTO(student);
    }

    @Override
    public StudentDTO editStudent(StudentDTO dto) {
        Student student = studentMapper.fromDTOToStudent(dto);
        student = studentRepository.save(student);
        return studentMapper.fromStudentToDTO(student);
    }

    @Override
    public void deleteStudentById(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public List<TaskDTO> getStudentTasks(Long id) {
        return getStudentById(id).getTasks().stream().map(taskMapper::fromTaskToDTO).toList();
    }
}
