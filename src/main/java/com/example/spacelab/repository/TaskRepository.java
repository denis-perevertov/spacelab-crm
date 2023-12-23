package com.example.spacelab.repository;

import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.task.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    @Query("SELECT t FROM Task t WHERE t.course.id IN :ids")
    List<Task> findAllByAllowedCourse(@Param("ids") Long... ids);

    @Query("SELECT t FROM Task t WHERE t.course.id IN :ids")
    Page<Task> findAllByAllowedCoursePage(Pageable pageable, @Param("ids") Long... ids);

    @Query("""
           SELECT t
           FROM Task t
           WHERE t.parentTask.id = :id
           """)
    List<Task> findTaskSubtasks(Long id);

    @Query("""
           SELECT t
           FROM Task t
           WHERE t.course.id = :id
           """)
    List<Task> getCourseTasks(Long id);

    @Query("""
           SELECT t
           FROM Task t
           WHERE t.course IS NULL
           AND t.parentTask IS NULL
           """)
    Page<Task> findAvailableParentTasks(Pageable pageable);

    List<Task> findTasksByParentTask(Task parentTask);
}
