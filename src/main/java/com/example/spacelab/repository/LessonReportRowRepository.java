package com.example.spacelab.repository;

import com.example.spacelab.model.lesson.LessonReportRow;
import com.example.spacelab.model.student.Student;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
@Hidden
@Repository
public interface LessonReportRowRepository extends JpaRepository<LessonReportRow, Long>, JpaSpecificationExecutor<LessonReportRow> {
//    Page<LessonReportRow> findAllByStudentId(Long studentId, Pageable pageable);

    List<LessonReportRow> findAllByStudent(Student student);

    @Query("""
            SELECT r
            FROM LessonReportRow r
            WHERE r.student = :st
            AND r.lesson.datetime >= :start AND r.lesson.datetime <= :end
            """)
    List<LessonReportRow> getAllStudentLessonReportsForPeriod(Student st, LocalDateTime start, LocalDateTime end);

    @Query("""
            SELECT AVG(r.rating)
            FROM LessonReportRow r
            WHERE r.student = :st
            """)
    int getAverageStudentRating(Student st);

    @Query("""
            SELECT AVG(r.rating)
            FROM LessonReportRow r
            WHERE r.student = :st
            AND r.lesson.datetime >= :from
            AND r.lesson.datetime <= :to
            """)
    double getAveragePeriodStudentRating(Student st, LocalDateTime from, LocalDateTime to);

    @Query("""
            SELECT AVG(r.hours)
            FROM LessonReportRow r
            WHERE r.student = :st
            """)
    Optional<Double> getAverageStudentLearningTime(Student st);

    @Query("""
            SELECT AVG(r.hours)
            FROM LessonReportRow r
            WHERE r.student IN :students
            """)
    Optional<Double> getAverageStudentLearningTime(Collection<Student> students);
}
