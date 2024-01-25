package com.example.spacelab.repository;

import com.example.spacelab.model.student.StudentTask;
import com.example.spacelab.model.student.StudentTaskStatus;
import com.example.spacelab.model.task.Task;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentTaskRepository extends JpaRepository<StudentTask, Long>, JpaSpecificationExecutor<StudentTask> {

    @Query("SELECT st " +
            "FROM StudentTask st " +
            "WHERE st.student.id=:studentID")
    List<StudentTask> findStudentTasks(Long studentID);

    @Query("SELECT st " +
            "FROM StudentTask st " +
            "WHERE st.student.id=:studentID AND st.status=:status")
    List<StudentTask> findStudentTasksWithStatus(Long studentID, StudentTaskStatus status);

    @Query("SELECT st " +
            "FROM StudentTask st " +
            "WHERE st.student.id=:studentID AND st.status=:status")
    Page<StudentTask> findStudentTasksWithStatusAndPage(Long studentID, StudentTaskStatus status, Pageable pageable);

    List<StudentTask> findStudentTasksByTaskReference(Task taskReference);
    void deleteStudentTasksByTaskReference(Task taskReference);

}
