package com.example.spacelab.service.impl;

import com.example.spacelab.dto.student.StudentLessonDisplayDTO;
import com.example.spacelab.exception.LessonException;
import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.model.lesson.Lesson;
import com.example.spacelab.model.lesson.LessonStatus;
import com.example.spacelab.model.settings.Settings;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.repository.LessonRepository;
import com.example.spacelab.service.LessonService;
import com.example.spacelab.service.SettingsService;
import com.example.spacelab.service.StudentService;
import com.example.spacelab.service.TaskService;
import com.example.spacelab.service.specification.LessonSpecifications;
import com.example.spacelab.util.AuthUtil;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    private final StudentService studentService;
    private final TaskService taskService;
    private final SettingsService settingsService;

    private final AuthUtil authUtil;

    @Override
    public List<Lesson> getLessons() {
        return lessonRepository.findAll();
    }

    @Override
    public Page<Lesson> getLessons(Pageable pageable) {
        return lessonRepository.findAll(pageable);
    }

    @Override
    public Page<Lesson> getLessons(FilterForm filters, Pageable pageable) {
        Specification<Lesson> spec = buildSpecificationFromFilters(filters);
        return lessonRepository.findAll(spec, pageable);
    }

    @Override
    public List<Lesson> getLessonsByAllowedCourses(Long... ids) {
        return lessonRepository.findAllByAllowedCourse(ids);
    }

    @Override
    public Page<Lesson> getLessonsByAllowedCourses(Pageable pageable, Long... ids) {
        return lessonRepository.findAllByAllowedCoursePage(pageable, ids);
    }

    @Override
    public long getCompletedLessonsCount() {
        return lessonRepository.getCompletedLessonsCount();
    }

    @Override
    public Page<Lesson> getLessonsByAllowedCourses(FilterForm filters, Pageable pageable, Long... ids) {
        Specification<Lesson> spec = buildSpecificationFromFilters(filters).and(LessonSpecifications.hasCourseIDs(ids));
        return lessonRepository.findAll(spec, pageable);
    }

    @Override
    public Lesson getLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        return lesson;
    }

    @Override
    public Lesson createNewLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }


    @Override
    public Lesson editLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    @Override
    public List<StudentLessonDisplayDTO> getStudentLessonDisplayData(Long id) {
        Lesson lesson = getLessonById(id);
        List<Student> courseStudents = lesson.getCourse().getStudents().stream().filter(Student::isEnabled).toList();
        List<StudentLessonDisplayDTO> lessonDisplayData = new ArrayList<>();
        courseStudents.forEach(st -> lessonDisplayData.add(new StudentLessonDisplayDTO(
                st.getId(),
                st.getFullName(),
                40.00,
                taskService.getOpenStudentTasks(st),
                taskService.getNextStudentTasks(st)
        )));
        return lessonDisplayData;
    }

    @Override
    public void deleteLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lesson with this ID doesn't exist!"));

        // recalculate average student rating after lesson delete
        if(!lesson.getStatus().equals(LessonStatus.PLANNED)) {
            studentService.recalculateAvgStudentRating(lesson.getCourse().getStudents());
        }

        lessonRepository.delete(lesson);
    }

    @Override
    public void startLesson(Long id) {
        Lesson lesson = getLessonById(id);
        log.info("starting lesson (id: {})", id);
        if(!lesson.getStatus().equals(LessonStatus.PLANNED)) throw new LessonException("Can't start an active/completed lesson!");
        lesson.setStatus(LessonStatus.ACTIVE);
        lessonRepository.save(lesson);
    }

    private void startLesson(Lesson lesson) {
        log.info("starting lesson (id: {})", lesson.getId());
        lesson.setStatus(LessonStatus.ACTIVE);
        lessonRepository.save(lesson);
    }

    @Override
    public void completeLesson(Long id) {
        Lesson lesson = getLessonById(id);
        if(!lesson.getStatus().equals(LessonStatus.ACTIVE)) throw new LessonException("Can't complete a completed/planned lesson!");
        lesson.setStatus(LessonStatus.COMPLETED);
        lessonRepository.save(lesson);

        // for every student on this lesson - recalculate avg rating
        studentService.recalculateAvgStudentRating(lesson.getCourse().getStudents());

        Settings settings = settingsService.getSettingsForAdmin(authUtil.getLoggedInAdmin());
        if(settings.isAutomaticLessonCreationSetting()) {
            duplicateAndSaveLesson(lesson, settings);
        };
    }

    private void duplicateAndSaveLesson(Lesson lesson, Settings settings) {
        lessonRepository.save(
                new Lesson()
                        .setLink(lesson.getLink())
                        .setCourse(lesson.getCourse())
                        .setStartsAutomatically(settings.isAutomaticLessonStartSetting())
                        .setStatus(LessonStatus.PLANNED)
                        .setDatetime(lesson.getDatetime().plusDays(lesson.getCourse().getCourseInfo().getLessonInterval()))
        );
    }

    @Scheduled(fixedRate = 1000 * 60)           // every 1 min
    public void checkAutomaticLessonStart() {
        log.info(" --- checking automatic lesson start --- ");

        LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC"));
        lessonRepository.findLessonsForAutomaticStart().forEach(lesson -> {
            if(Duration.between(lesson.getDatetime(), now).toMinutes() >= 0) {
                startLesson(lesson);
            }
        });
    }

    @Override
    public Specification<Lesson> buildSpecificationFromFilters(FilterForm filters) {

        log.info("Building specification from filters: " + filters);

        String begin = filters.getBegin();
        String end = filters.getEnd();
        Long course = filters.getCourse();
        String statusInput = filters.getStatus();
        Long mentor = filters.getMentor();
        Long manager = filters.getManager();
        Long admin = filters.getAdmin();

        LocalDateTime beginDate = null, endDate = null;

        if(begin != null && !begin.isEmpty() && end != null && !end.isEmpty()) {
            beginDate = LocalDate.parse(begin, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
            endDate = LocalDate.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atTime(LocalTime.MAX);
        }

        LessonStatus status = StringUtils.fieldIsEmpty(statusInput) ? null : LessonStatus.valueOf(statusInput);

        Specification<Lesson> spec = LessonSpecifications.hasDatesBetween(beginDate, endDate)
                                    .and(LessonSpecifications.hasCourseId(course))
                                    .and(LessonSpecifications.hasStatus(status))
                                    .and(LessonSpecifications.hasAdminId(admin))
                                    .and(LessonSpecifications.hasManagerId(manager))
                                    .and(LessonSpecifications.hasMentorId(mentor));

        return spec;
    }
}
