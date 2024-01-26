package com.example.spacelab.service.impl;

import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.model.lesson.Lesson;
import com.example.spacelab.model.lesson.LessonReportRow;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.student.StudentTask;
import com.example.spacelab.dto.lesson.LessonReportRowSaveDTO;
import com.example.spacelab.repository.LessonReportRowRepository;
import com.example.spacelab.repository.LessonRepository;
import com.example.spacelab.repository.StudentTaskRepository;
import com.example.spacelab.service.LessonReportRowService;
import com.example.spacelab.model.student.StudentTaskStatus;
import com.example.spacelab.service.LessonService;
import com.example.spacelab.service.StudentService;
import com.example.spacelab.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Log
@RequiredArgsConstructor
public class LessonReportRowServiceImpl implements LessonReportRowService {

    private final LessonService lessonService;
    private final LessonRepository lessonRepository;
    private final StudentService studentService;
    private final LessonReportRowRepository lessonReportRowRepository;
    private final StudentTaskRepository studentTaskRepository;
    private final TaskService taskService;

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
    public LessonReportRow updateLessonReportRow(LessonReportRowSaveDTO dto) {
        Student st = studentService.getStudentById(dto.getStudentId());
        String currentTaskSnapshot = String.join(", ", st.getTasks()
                        .stream()
                        .filter(t -> t.getStatus().equals(StudentTaskStatus.UNLOCKED) || t.getStatus().equals(StudentTaskStatus.READY))
                        .map(t -> t.getTaskReference().getName() + " (" + t.getPercentOfCompletion() + "%)")
                        .toList());
//                    .reduce("", (a, b) -> String.join(",", a, b));
        LessonReportRow lessonReportRow =
                lessonReportRowRepository.findById(dto.getId()).orElse(new LessonReportRow())
                        .setWasPresent(dto.getWasPresent())
                        .setHours(dto.getHours())
                        .setHoursNote(dto.getNote())
                        .setComment(dto.getComment())
                        .setRating(dto.getRating())
                        .setStudent(st)
                        .setCurrentTaskSnapshot(currentTaskSnapshot);
//        lessonReportRow = lessonReportRowRepository.save(lessonReportRow);
//        log.info("saved lesson report row");

        Lesson lesson = lessonService.getLessonById(dto.getLessonId());
        lesson.getReportRows().add(lessonReportRow);
        lessonReportRow.setLesson(lesson);
        lessonRepository.save(lesson);
        log.info("added report row to lesson entity");

        log.info("checking completed tasks for student");
        dto.getCompletedTasksIds().forEach(taskService::completeStudentTask);

        log.info("checking unlocked tasks for student");
        dto.getUnlockedTasksIds().forEach(taskService::unlockStudentTask);

        return lessonReportRow;
    }

}
