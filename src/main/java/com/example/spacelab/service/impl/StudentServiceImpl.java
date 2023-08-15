package com.example.spacelab.service.impl;

import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.mapper.StudentDTOMapper;
import com.example.spacelab.mapper.TaskDTOMapper;
import com.example.spacelab.model.Course;
import com.example.spacelab.model.Student;
import com.example.spacelab.model.dto.StudentDTO;
import com.example.spacelab.model.dto.TaskDTO;
import com.example.spacelab.repository.CourseRepository;
import com.example.spacelab.repository.StudentRepository;
import com.example.spacelab.service.StudentService;
import com.example.spacelab.service.specification.StudentSpecifications;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.util.StudentAccountStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    private final StudentDTOMapper studentMapper;
    private final TaskDTOMapper taskMapper;

    @Override
    public List<StudentDTO> getStudents() {
        return studentRepository.findAll().stream().map(studentMapper::fromStudentToDTO).toList();
    }

    @Override
    public List<StudentDTO> getStudents(Pageable pageable) {
        return studentRepository.findAll(pageable).get().map(studentMapper::fromStudentToDTO).toList();
    }

    public List<StudentDTO> getStudents(FilterForm filters, Pageable pageable) {
        Specification<Student> spec = buildSpecificationFromFilters(filters);
        return studentRepository.findAll(spec, pageable).get().map(studentMapper::fromStudentToDTO).toList();
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
//
//    @Override
//    public List<TaskDTO> getStudentTasks(Long id) {
//        return getStudentById(id).getTasks().stream().map(taskMapper::fromTaskToDTO).toList();
//    }

    @Override
    public List<TaskDTO> getStudentTasks(Long id) {
        return null;
    }

    @Override
    public Specification<Student> buildSpecificationFromFilters(FilterForm filters) {

        String nameEmailInput = filters.getName();
        Long courseID = filters.getCourse();
        String phoneInput = filters.getPhone();
        String telegramInput = filters.getTelegram();
        Integer ratingInput = filters.getRating();
        String statusInput = filters.getStatus();

        Course course = (courseID == null) ? null : courseRepository.getReferenceById(courseID);
        StudentAccountStatus status = (statusInput == null) ? null : StudentAccountStatus.valueOf(statusInput);

        Specification<Student> spec = Specification.where(StudentSpecifications.hasNameOrEmailLike(nameEmailInput)
                                                    .and(StudentSpecifications.hasCourse(course))
                                                    .and(StudentSpecifications.hasPhoneLike(phoneInput))
                                                    .and(StudentSpecifications.hasTelegramLike(telegramInput))
                                                    .and(StudentSpecifications.hasRatingOrHigher(ratingInput))
                                                    .and(StudentSpecifications.hasStatus(status)));

        return spec;
    }
}
