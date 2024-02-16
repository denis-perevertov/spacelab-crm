package com.example.spacelab.service;

import com.example.spacelab.dto.statistics.StudentLearningTimeGraphDTO;
import com.example.spacelab.dto.statistics.StudentRatingGraphDTO;
import com.example.spacelab.dto.statistics.StudentRatingTableDTO;
import com.example.spacelab.dto.statistics.StudentStatisticsDTO;
import com.example.spacelab.integration.data.TimeTotalResponse;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.lesson.Lesson;
import com.example.spacelab.model.lesson.LessonReportRow;
import com.example.spacelab.model.student.StudentInviteRequest;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.student.StudentTask;
import com.example.spacelab.util.FilterForm;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
@Hidden
public interface StudentService extends StudentCardService,
                                        EntityFilterService<Student> {

    List<Student> getStudents();
    Page<Student> getStudents(Pageable pageable);
    Page<Student> getStudents(FilterForm filters, Pageable pageable);
    Page<Student> getAvailableStudents(FilterForm filters, Pageable pageable);
    Page<Student> getStudentsWithoutCourses(FilterForm filters, Pageable pageable);

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

    void transferStudentToCourse(Student student, Course course);

    List<LessonReportRow> getStudentLessonData(Long studentID);
    Long getStudentCourseID(Long studentID);

    void recalculateAvgStudentRating(Student student);
    void recalculateAvgStudentRating(Collection<Student> students);

    double getAvgStudentLearningTime(Student student);
    double getAvgStudentLearningTime(Collection<Student> students);

    List<StudentStatisticsDTO> getStudentDistributionByGender();
    List<StudentStatisticsDTO> getStudentDistributionByWorkStatus();
    List<StudentStatisticsDTO> getStudentDistributionByAccountStatus();
    List<StudentStatisticsDTO> getStudentDistributionByEnglishLevel();
    List<StudentStatisticsDTO> getStudentDistributionByEducationLevel();
    List<StudentStatisticsDTO> getStudentHiredRatio();

    Page<StudentRatingTableDTO> getStudentsSortedByRating(int limit, Sort.Direction direction);

    double getStudentTotalAverageRating(Long studentId);
    double getStudentRecentAverageRating(Long studentId);

    TimeTotalResponse getStudentTotalLearningTime(Long studentId);
    TimeTotalResponse getStudentRecentLearningTime(Long studentId);

    Optional<StudentTask> getStudentLastCompletedTask(Long studentId);
    long getStudentCompletedTaskAmount(Long studentId);

    Optional<Lesson> getStudentLastVisitedLesson(Long studentId);
    Optional<Lesson> getStudentNextLesson(Long studentId);
    long getStudentVisitedLessonAmount(Long studentId);
    long getStudentSkippedLessonAmount(Long studentId);

    long getActiveStudentsCount();
    long getHiredStudentsCount();
    long getExpelledStudentsCount();
    long getTotalStudentsCount();

    StudentLearningTimeGraphDTO getStudentLearningTimeDistribution(Long studentId, LocalDateTime from, LocalDateTime to);
    StudentRatingGraphDTO getStudentRatingDistribution(Long studentId, LocalDateTime from, LocalDateTime to);


}
