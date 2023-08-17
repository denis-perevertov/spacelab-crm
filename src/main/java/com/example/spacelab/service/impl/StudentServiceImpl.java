package com.example.spacelab.service.impl;

import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.mapper.StudentMapper;
import com.example.spacelab.mapper.TaskMapper;
import com.example.spacelab.model.Course;
import com.example.spacelab.model.InviteStudentRequest;
import com.example.spacelab.model.Student;
import com.example.spacelab.model.StudentTask;
import com.example.spacelab.model.dto.StudentDTO;
import com.example.spacelab.model.dto.StudentTaskDTO;
import com.example.spacelab.model.dto.TaskDTO;
import com.example.spacelab.repository.*;
import com.example.spacelab.service.StudentService;
import com.example.spacelab.service.specification.StudentSpecifications;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.util.StudentAccountStatus;
import com.example.spacelab.util.StudentTaskStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Log
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final InviteStudentRequestRepository inviteRepository;
    private final StudentTaskRepository studentTaskRepository;

    private final StudentMapper studentMapper;
    private final TaskMapper taskMapper;

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


    /*
        TODO
        пагинация заданий
     */
    @Override
    public List<StudentTaskDTO> getStudentTasks(Long studentID) {
        return studentTaskRepository.findStudentTasks(studentID)
                .stream()
                .map(taskMapper::fromStudentTaskToStudentTaskDTO)
                .toList();
    }

    @Override
    public List<StudentTaskDTO> getStudentTasks(Long studentID, StudentTaskStatus status) {
        return studentTaskRepository.findStudentTasks(studentID, status)
                .stream()
                .map(taskMapper::fromStudentTaskToStudentTaskDTO)
                .toList();
    }


    @Override
    public StudentTaskDTO getStudentTask(Long taskID) {
        StudentTask task = studentTaskRepository.findById(taskID).orElseThrow(() -> new ResourceNotFoundException("Student task not found"));
        return taskMapper.fromStudentTaskToStudentTaskDTO(task);
    }

    @Override
    public String createInviteStudentToken(InviteStudentRequest request) {
        UUID id = UUID.randomUUID();
        request.setId(id.toString());
        request = inviteRepository.save(request);
        return request.getId();
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
