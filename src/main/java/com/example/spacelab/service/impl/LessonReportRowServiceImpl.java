package com.example.spacelab.service.impl;

import com.example.spacelab.dto.lesson.LessonReportRowSaveDTO;
import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.model.lesson.Lesson;
import com.example.spacelab.model.lesson.LessonReportRow;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.student.StudentTaskStatus;
import com.example.spacelab.repository.LessonReportRowRepository;
import com.example.spacelab.repository.LessonRepository;
import com.example.spacelab.repository.StudentTaskRepository;
import com.example.spacelab.service.LessonReportRowService;
import com.example.spacelab.service.LessonService;
import com.example.spacelab.service.StudentService;
import com.example.spacelab.service.TaskService;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.example.spacelab.service.specification.LessonReportSpecifications.*;

@Service
@Slf4j
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
        log.info("ROW: {}", dto.toString());
        Student st = studentService.getStudentById(dto.getStudentId());
        String currentTaskSnapshot = String.join(", ", st.getTasks()
                        .stream()
                        .filter(t -> t.getStatus().equals(StudentTaskStatus.UNLOCKED) || t.getStatus().equals(StudentTaskStatus.READY))
                        .map(t -> t.getTaskReference().getName() + " (" + t.getPercentOfCompletion() + "%)")
                        .toList());
//                    .reduce("", (a, b) -> String.join(",", a, b));
        LessonReportRow lessonReportRow =
//                lessonReportRowRepository.findById(dto.getId()).orElse(new LessonReportRow())
                        new LessonReportRow()
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

    @Override
    public Page<LessonReportRow> getStudentLessonReports(FilterForm filters, Pageable pageable) {
        Specification<LessonReportRow> spec = buildSpecificationFromFilters(filters);
        Page<LessonReportRow> page = lessonReportRowRepository.findAll(spec, pageable);
        log.info("fetched lesson report rows for student(id:{}) - page {} / {}", filters.getStudent(), pageable.getPageNumber(), page.getTotalPages());
        return page;
    }

    @Override
    public Specification<LessonReportRow> buildSpecificationFromFilters(FilterForm filters) {
        Long student = filters.getStudent();
        Boolean present = filters.getPresent();
        String beginDatetime = filters.getBegin();
        String endDatetime = filters.getEnd();
        Long task = filters.getTask();
        Integer hoursFrom = filters.getHoursFrom();
        Integer hoursTo = filters.getHoursTo();
        String note = filters.getNote();
        String comment = filters.getComment();
        Integer ratingFrom = filters.getRatingFrom();
        Integer ratingTo = filters.getRatingTo();

        LocalDateTime begin = (ValidationUtils.fieldIsEmpty(beginDatetime)) ? null : LocalDate.parse(beginDatetime, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
        LocalDateTime end = (ValidationUtils.fieldIsEmpty(endDatetime)) ? null : LocalDate.parse(endDatetime, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atTime(LocalTime.MAX);

        Specification<LessonReportRow> spec =
                hasStudentId(student)
                .and(hasDatesBetween(begin, end))
                .and(wasPresent(present))
                .and(hasHoursBetween(hoursFrom, hoursTo))
                .and(hasRatingBetween(ratingFrom, ratingTo))
                .and(hasNoteLike(note))
                .and(hasCommentLike(comment));

        return spec;
    }
}
