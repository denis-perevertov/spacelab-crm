package com.example.spacelab.service;

import com.example.spacelab.model.lesson.LessonReportRow;
import com.example.spacelab.dto.lesson.LessonReportRowSaveDTO;

public interface LessonReportRowService {

    LessonReportRow getLessonReportRowById(Long id);
    LessonReportRow createNewLessonReportRow(LessonReportRow lessonReportRow);
    LessonReportRow updateLessonReportRow(LessonReportRowSaveDTO lessonReportRowSaveDTO);
    void deleteLessonReportRowById(Long id);
}
