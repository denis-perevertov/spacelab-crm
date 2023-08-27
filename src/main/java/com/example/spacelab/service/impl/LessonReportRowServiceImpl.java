package com.example.spacelab.service.impl;

import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.model.lesson.LessonReportRow;
import com.example.spacelab.model.student.StudentTask;
import com.example.spacelab.dto.lesson.LessonReportRowSaveDTO;
import com.example.spacelab.repository.LessonReportRowRepository;
import com.example.spacelab.repository.StudentTaskRepository;
import com.example.spacelab.service.LessonReportRowService;
import com.example.spacelab.model.student.StudentTaskStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Log
@RequiredArgsConstructor
public class LessonReportRowServiceImpl implements LessonReportRowService {

    private final LessonReportRowRepository lessonReportRowRepository;
    private final StudentTaskRepository studentTaskRepository;

    @Override
    public LessonReportRow getLessonReportRowById(Long id) {
        LessonReportRow lessonReportRow = lessonReportRowRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        return lessonReportRow;
    }

    @Override
    public LessonReportRow createNewLessonReportRow(LessonReportRow lessonReportRow) {
        return lessonReportRowRepository.save(lessonReportRow);
    }
    @Override
    public void deleteLessonReportRowById(Long id) {
        lessonReportRowRepository.deleteById(id);
    }

    @Override
    public void updateLessonReportRowAndCompletedTask(LessonReportRowSaveDTO lessonReportRowSaveDTO) {
        LessonReportRow lessonReportRow = lessonReportRowRepository.findById(lessonReportRowSaveDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        lessonReportRow.setWasPresent(lessonReportRowSaveDTO.getWasPresent());
        lessonReportRow.setHours(lessonReportRowSaveDTO.getHours());
        lessonReportRow.setHoursNote(lessonReportRowSaveDTO.getHoursNote());
        lessonReportRow.setComment(lessonReportRowSaveDTO.getComment());
        lessonReportRow.setRating(lessonReportRowSaveDTO.getRating());
        lessonReportRowRepository.save(lessonReportRow);

        for (Map.Entry<Long, Boolean> entry : lessonReportRowSaveDTO.getCompletedTasks().entrySet()) {
            if (entry.getValue() == true) {
               StudentTask studentTask = studentTaskRepository.findById(entry.getKey()).orElseThrow(() -> new ResourceNotFoundException("StudentTask task not found"));
                studentTask.setStatus(StudentTaskStatus.COMPLETED);
                studentTaskRepository.save(studentTask);
            }
        }
    }

}
