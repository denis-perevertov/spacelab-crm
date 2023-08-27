package com.example.spacelab.repository;

import com.example.spacelab.model.lesson.LessonReportRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonReportRowRepository extends JpaRepository<LessonReportRow, Long> {
}
