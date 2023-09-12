package com.example.spacelab.service;

import com.example.spacelab.model.lesson.LessonReportRow;
import com.example.spacelab.model.student.StudentInviteRequest;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.util.FilterForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService extends StudentTaskService,
                                        StudentCardService,
                                        EntityFilterService<Student> {

    List<Student> getStudents();
    Page<Student> getStudents(Pageable pageable);
    Page<Student> getStudents(FilterForm filters, Pageable pageable);

    // filter by allowed courses
    List<Student> getStudentsByAllowedCourses(Long... ids);
    Page<Student> getStudentsByAllowedCourses(Pageable pageable, Long... ids);
    Page<Student> getStudentsByAllowedCourses(FilterForm filters, Pageable pageable, Long... ids);

    Student getStudentById(Long id);
    Student createNewStudent(Student student);
    Student registerStudent(Student student);
    Student editStudent(Student student);

    void deleteStudentById(Long id);

    String createInviteStudentToken(StudentInviteRequest request);


    List<LessonReportRow> getStudentLessonData(Long studentID);
    Long getStudentCourseID(Long studentID);
}
