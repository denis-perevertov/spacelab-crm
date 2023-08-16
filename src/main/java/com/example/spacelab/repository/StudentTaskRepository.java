package com.example.spacelab.repository;

import com.example.spacelab.model.StudentTask;
import com.example.spacelab.util.StudentTaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentTaskRepository extends JpaRepository<StudentTask, Long> {
    @Query("SELECT st from StudentTask st WHERE st.student.id=:studentID")
    List<StudentTask> findStudentTasks(Long studentID);

    @Query("SELECT st from StudentTask st WHERE st.student.id=:studentID AND st.status=:status")
    List<StudentTask> findStudentTasks(Long studentID, StudentTaskStatus status);
}
