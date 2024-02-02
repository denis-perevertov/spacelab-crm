package com.example.spacelab.repository;

import com.example.spacelab.model.student.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    @Query("SELECT s FROM Student s WHERE s.course.id IN :ids")
    List<Student> findAllByAllowedCourse(@Param("ids") Long... ids);

    @Query("SELECT s FROM Student s WHERE s.course.id IN :ids")
    Page<Student> findAllByAllowedCoursePage(Pageable pageable, @Param("ids") Long... ids);

    Optional<Student> findByDetailsEmail(String email);
    boolean existsByDetailsEmail(String email);

    @Query("""
            UPDATE Student s
            SET s.learningDuration = s.learningDuration + 1
            WHERE s.details.accountStatus = ACTIVE
            """)
    @Transactional
    @Modifying
    void addLearningDayForActiveStudents();

    @Query("""
            SELECT COUNT(s)
            FROM Student s
            WHERE s.details.accountStatus = ACTIVE
            """)
    long getActiveStudentsCount();

    @Query("""
            SELECT COUNT(s)
            FROM Student s
            WHERE s.details.accountStatus = HIRED
            """)
    long getHiredStudentsCount();

    @Query("""
            SELECT COUNT(s)
            FROM Student s
            WHERE s.details.accountStatus = EXPELLED
            """)
    long getExpelledStudentsCount();
}
