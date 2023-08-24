package com.example.spacelab.repository;

import com.example.spacelab.model.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

//    @Query("SELECT CASE WHEN EXISTS(SELECT std FROM Student std WHERE std.details.email=:email) THEN TRUE ELSE FALSE END " +
//            "FROM Student st")
//    boolean existsByEmail(String email);

    boolean existsByDetailsEmail(String email);
}
