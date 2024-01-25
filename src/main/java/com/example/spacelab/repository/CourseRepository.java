package com.example.spacelab.repository;

import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.student.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    boolean existsByName(String name);

    @Query("SELECT c FROM Course c WHERE c.id IN :ids")
    List<Course> findAllowedCourses(@Param("ids") Long... ids);

    @Query("SELECT c FROM Course c WHERE c.id IN :ids")
    Page<Course> findAllowedCoursesPage(Pageable pageable, @Param("ids") Long... ids);

    Optional<Course> findByName(String name);
}
