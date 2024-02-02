package com.example.spacelab.service;

import com.example.spacelab.model.lesson.LessonReportRow;
import com.example.spacelab.dto.lesson.LessonReportRowSaveDTO;
import com.example.spacelab.util.FilterForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface LessonReportRowService extends EntityFilterService<LessonReportRow> {

    LessonReportRow getLessonReportRowById(Long id);
    LessonReportRow createNewLessonReportRow(LessonReportRow lessonReportRow);
    LessonReportRow updateLessonReportRow(LessonReportRowSaveDTO lessonReportRowSaveDTO);

    Page<LessonReportRow> getStudentLessonReports(FilterForm filters, Pageable pageable);

    void deleteLessonReportRowById(Long id);
}
