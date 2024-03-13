package com.example.spacelab.service.impl;

import com.example.spacelab.dto.statistics.StudentLearningTimeGraphDTO;
import com.example.spacelab.dto.statistics.StudentRatingGraphDTO;
import com.example.spacelab.dto.statistics.StudentRatingTableDTO;
import com.example.spacelab.dto.statistics.StudentStatisticsDTO;
import com.example.spacelab.dto.student.StudentCardDTO;
import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.integration.TaskTrackingService;
import com.example.spacelab.integration.data.TimeTotalResponse;
import com.example.spacelab.mapper.StudentMapper;
import com.example.spacelab.mapper.TaskMapper;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.lesson.Lesson;
import com.example.spacelab.model.lesson.LessonReportRow;
import com.example.spacelab.model.lesson.LessonStatus;
import com.example.spacelab.model.student.*;
import com.example.spacelab.repository.*;
import com.example.spacelab.service.StudentService;
import com.example.spacelab.service.TaskService;
import com.example.spacelab.service.specification.StudentSpecifications;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.util.NumericUtils;
import com.example.spacelab.util.StringUtils;
import com.example.spacelab.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final InviteStudentRequestRepository inviteRepository;
    private final StudentTaskRepository studentTaskRepository;
    private final UserRoleRepository userRoleRepository;
    private final LessonReportRowRepository lessonReportRowRepository;

    private final TaskService taskService;

    private final TaskTrackingService trackingService;

    private final StudentMapper studentMapper;
    private final TaskMapper taskMapper;

    @Override
    public List<Student> getStudents() {
        log.info("Getting all students' info without filters or pages...");
        return studentRepository.findAll();
    }

    @Override
    public Page<Student> getStudents(Pageable pageable) {
        log.info("Getting all students' info with page " + pageable.getPageNumber() +
                " / size " + pageable.getPageSize());
        return studentRepository.findAll(pageable);
    }

    public Page<Student> getStudents(FilterForm filters, Pageable pageable) {
        log.info("Getting all students' info with page " + pageable.getPageNumber() +
                " / size " + pageable.getPageSize() + " and filters: " + filters);
        Specification<Student> spec = buildSpecificationFromFilters(filters);
        return studentRepository.findAll(spec, pageable);
    }

    @Override
    public Page<Student> getAvailableStudents(FilterForm filters, Pageable pageable) {
        log.info("Getting non-blocked students' info with page " + pageable.getPageNumber() +
                " / size " + pageable.getPageSize() + " and filters: " + filters);
        Specification<Student> spec = buildSpecificationFromFilters(filters).and(StudentSpecifications.isAvailable());
        return studentRepository.findAll(spec, pageable);
    }

    @Override
    public Page<Student> getStudentsWithoutCourses(FilterForm filters, Pageable pageable) {
        log.info("Getting all students without courses with page {} / size {} and filters: {}",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                filters);
        Specification<Student> noCourseSpec = (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("course"));
        Specification<Student> generalSpec = buildSpecificationFromFilters(filters).and(noCourseSpec);
        return studentRepository.findAll(generalSpec, pageable);
    }

    public List<Student> getStudentsByAllowedCourses(Long... ids) {
        log.info("Getting all students' info without filters or pages | for courses with IDs: " + Arrays.toString(ids));
        return studentRepository.findAllByAllowedCourse(ids);
    }

    public Page<Student> getStudentsByAllowedCourses(Pageable pageable, Long... ids) {
        log.info("Getting all students' info with page " + pageable.getPageNumber() +
                " / size " + pageable.getPageSize() + " | for courses with IDs: " + Arrays.toString(ids));
        return studentRepository.findAllByAllowedCoursePage(pageable, ids);
    }

    public Page<Student> getStudentsByAllowedCourses(FilterForm filters, Pageable pageable, Long... ids) {
        log.info("Getting all students' info with page " + pageable.getPageNumber() +
                " / size " + pageable.getPageSize() + " and filters: " + filters + " | for courses with IDs: " + Arrays.toString(ids));
        Specification<Student> spec = buildSpecificationFromFilters(filters).and(StudentSpecifications.hasCourseIDs(ids));
        return studentRepository.findAll(spec, pageable);
    }

    @Override
    public Student getStudentById(Long id) {
        log.info("Getting student with ID: " + id);
        Student student = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student not found", Student.class));
        return student;
    }

    @Override
    public Student createNewStudent(Student student) {

        student.setRating(0);
        student.setRole(userRoleRepository.getReferenceByName("STUDENT"));
        student.getDetails().setAccountStatus(StudentAccountStatus.ACTIVE);

        student = studentRepository.save(student);
        log.info("Created student: " + student);
        return student;
    }

    @Override
    public Student registerStudent(Student student) {

        student.setRating(0);
        student.setRole(userRoleRepository.getReferenceByName("STUDENT"));
        student.getDetails().setAccountStatus(StudentAccountStatus.ACTIVE);

        student = studentRepository.save(student);
        log.info("Created student: " + student);
        return student;
    }

    @Override
    public Student editStudent(Student student) {

        student = studentRepository.save(student);
        log.info("Edited student: " + student);
        return student;
    }


    @Scheduled(fixedRate = 1000 * 60 * 60 * 24, initialDelay = 1000 * 60 * 60 * 24) // every 24h
    @Async
    public void recalculateStudentLearningDuration() {
        log.info("=== scheduled recalculating of student learning duration ===");
        studentRepository.addLearningDayForActiveStudents();
    }


    @Override
    public StudentCardDTO getCard(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student not found", Student.class));
        return studentMapper.fromStudentToCardDTO(student);
    }

    @Override
    public void deleteStudentById(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student not found", Student.class));
        log.info("Deleting student with ID: " + id);
        studentRepository.delete(student);
    }

    @Override
    public String createInviteStudentToken(StudentInviteRequest request) {
        log.info("Creating token to use in Student invite URL...");
        UUID token = UUID.randomUUID();
        request.setToken(token.toString());
        log.info("Created token: " + token + ", saving token with set parameters in DB");
        inviteRepository.save(request);
        return token.toString();
    }

    @Override
    public void transferStudentToCourse(Student student, Course course) {
        student.setCourse(course);
        taskService.createStudentTasksOnStudentCourseTransfer(student, course);
    }



    @Override
    public List<LessonReportRow> getStudentLessonData(Long studentID) {
//        return getStudentById(studentID).getLessonData();
        return new ArrayList<>();
    }

    @Override
    public Long getStudentCourseID(Long studentID) {
        Course studentCourse = getStudentById(studentID).getCourse();
        return (studentCourse != null) ? studentCourse.getId() : null;
    }

    @Override
    public void recalculateAvgStudentRating(Student student) {
        student.setRating(lessonReportRowRepository.getAverageStudentRating(student));
        studentRepository.save(student);
    }

    @Override
    public void recalculateAvgStudentRating(Collection<Student> students) {
        students.stream().filter(Student::isEnabled).forEach(st -> st.setRating(lessonReportRowRepository.getAverageStudentRating(st)));
        studentRepository.saveAll(students);
    }

    @Override
    public double getAvgStudentLearningTime(Student student) {
        return lessonReportRowRepository.getAverageStudentLearningTime(student).orElse(0.0);
    }

    @Override
    public double getAvgStudentLearningTime(Collection<Student> students) {
        return lessonReportRowRepository.getAverageStudentLearningTime(students).orElse(0.0);
    }

    // todo
    @Override
    public List<StudentStatisticsDTO> getStudentDistributionByGender() {
        return new ArrayList<>();
    }

    @Override
    public long getActiveStudentsCount() {
        return studentRepository.getActiveStudentsCount();
    }

    @Override
    public long getHiredStudentsCount() {
        return studentRepository.getHiredStudentsCount();
    }

    @Override
    public long getExpelledStudentsCount() {
        return studentRepository.getExpelledStudentsCount();
    }

    @Override
    public long getTotalStudentsCount() {
        return studentRepository.count();
    }

    @Override
    public StudentLearningTimeGraphDTO getStudentLearningTimeDistribution(Long studentId, LocalDateTime from, LocalDateTime to) {
        Student st = getStudentById(studentId);
        LocalDateTime start = Optional.ofNullable(from).orElse(st.getCreatedAt());
        LocalDateTime end = Optional.ofNullable(to).orElse(LocalDateTime.now());
        List<LessonReportRow> list = lessonReportRowRepository.getAllStudentLessonReportsForPeriod(st, start, end);
        return new StudentLearningTimeGraphDTO(
                list.stream().sorted(Comparator.comparing(r -> r.getLesson().getDatetime())).map(r -> r.getLesson().getDatetime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))).toList(),
                list.stream().sorted(Comparator.comparing(r -> r.getLesson().getDatetime())).map(LessonReportRow::getHours).toList()
        );
    }

    @Override
    public StudentRatingGraphDTO getStudentRatingDistribution(Long studentId, LocalDateTime from, LocalDateTime to) {
        Student st = getStudentById(studentId);
        LocalDateTime start = Optional.ofNullable(from).orElse(st.getCreatedAt());
        LocalDateTime end = Optional.ofNullable(to).orElse(LocalDateTime.now());
        List<LessonReportRow> list = lessonReportRowRepository.getAllStudentLessonReportsForPeriod(st, start, end);
        return new StudentRatingGraphDTO(
                list.stream().sorted(Comparator.comparing(r -> r.getLesson().getDatetime())).map(r -> r.getLesson().getDatetime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))).toList(),
                list.stream().sorted(Comparator.comparing(r -> r.getLesson().getDatetime())).map(LessonReportRow::getRating).toList()
        );
    }

    @Override
    public double getStudentTotalAverageRating(Long studentId) {
        Student st = getStudentById(studentId);
        return st.getRating();
    }

    @Override
    public double getStudentRecentAverageRating(Long studentId) {
        Student st = getStudentById(studentId);
        LocalDateTime from = LocalDateTime.now().withDayOfMonth(1);
        LocalDateTime to = LocalDateTime.now().plusMonths(1).withDayOfMonth(1);
        return lessonReportRowRepository.getAveragePeriodStudentRating(st, from, to);
    }

    @Override
    public TimeTotalResponse getStudentTotalLearningTime(Long studentId) {
        Student st = getStudentById(studentId);
        if(ValidationUtils.fieldIsEmpty(st.getTaskTrackingProfileId())) {
            return null;
        }
        else {
            return trackingService.getUserTotalTime(st.getTaskTrackingProfileId());
        }
    }

    @Override
    public TimeTotalResponse getStudentRecentLearningTime(Long studentId) {
        Student st = getStudentById(studentId);
        if(ValidationUtils.fieldIsEmpty(st.getTaskTrackingProfileId())) {
            return null;
        }
        else {
            return trackingService.getUserTotalTimeRecent(st.getTaskTrackingProfileId());
        }
    }

    @Override
    public Optional<StudentTask> getStudentLastCompletedTask(Long studentId) {
        Student st = getStudentById(studentId);
        return st.getTasks()
                .stream()
                .filter(t -> t.getStatus().equals(StudentTaskStatus.COMPLETED))
                .sorted(Comparator.comparing(StudentTask::getEndDate).reversed())
                .limit(1)
                .findAny();
    }

    @Override
    public long getStudentCompletedTaskAmount(Long studentId) {
        Student st = getStudentById(studentId);
        return st.getTasks()
                .stream()
                .filter(t -> t.getStatus().equals(StudentTaskStatus.COMPLETED))
                .count();
    }

    @Override
    public Optional<Lesson> getStudentLastVisitedLesson(Long studentId) {
        Student st = getStudentById(studentId);
        return lessonRepository.findAllByCourse(st.getCourse())
                .stream()
                .filter(l -> l.getStatus().equals(LessonStatus.COMPLETED))
                .sorted(Comparator.comparing(Lesson::getDatetime).reversed())
                .limit(1)
                .findAny();
    }

    @Override
    public Optional<Lesson> getStudentNextLesson(Long studentId) {
        Student st = getStudentById(studentId);
        return lessonRepository.findAllByCourse(st.getCourse())
                .stream()
                .filter(l -> l.getStatus().equals(LessonStatus.PLANNED))
                .sorted(Comparator.comparing(Lesson::getDatetime))
                .limit(1)
                .findAny();
    }

    // get lesson report row
    @Override
    public long getStudentVisitedLessonAmount(Long studentId) {
        Student st = getStudentById(studentId);
        return lessonReportRowRepository.findAllByStudent(st)
                .stream()
                .filter(LessonReportRow::getWasPresent)
                .count();
    }

    @Override
    public long getStudentSkippedLessonAmount(Long studentId) {
        Student st = getStudentById(studentId);
        return lessonReportRowRepository.findAllByStudent(st)
                .stream()
                .filter(r -> !r.getWasPresent())
                .count();
    }

    @Override
    public List<StudentStatisticsDTO> getStudentDistributionByWorkStatus() {
        List<StudentStatisticsDTO> list = new ArrayList<>();
        List<Student> students = studentRepository.findAll();
        for(StudentWorkStatus status : StudentWorkStatus.values()) {
            list.add(
                    new StudentStatisticsDTO(
                            status.name(),
                            new Object[]{students.stream().filter(st -> st.getDetails().getWorkStatus().equals(status)).count()}
                    )
            );
        }
        return list;
    }

    @Override
    public List<StudentStatisticsDTO> getStudentDistributionByAccountStatus() {
        List<StudentStatisticsDTO> list = new ArrayList<>();
        List<Student> students = studentRepository.findAll();
        for(StudentAccountStatus status : StudentAccountStatus.values()) {
            list.add(
                    new StudentStatisticsDTO(
                            status.name(),
                            new Object[]{students.stream().filter(st -> st.getDetails().getAccountStatus().equals(status)).count()}
                    )
            );
        }
        return list;
    }

    @Override
    public List<StudentStatisticsDTO> getStudentDistributionByEnglishLevel() {
        List<StudentStatisticsDTO> list = new ArrayList<>();
        List<Student> students = studentRepository.findAll();
        for(StudentEnglishLevel level : StudentEnglishLevel.values()) {
            list.add(
                    new StudentStatisticsDTO(
                            level.name(),
                            new Object[]{students.stream().filter(st -> st.getDetails().getEnglishLevel().equals(level)).count()}
                    )
            );
        }
        return list;
    }

    @Override
    public List<StudentStatisticsDTO> getStudentDistributionByEducationLevel() {
        List<StudentStatisticsDTO> list = new ArrayList<>();
        List<Student> students = studentRepository.findAll();
        for(StudentEducationLevel level : StudentEducationLevel.values()) {
            list.add(
                    new StudentStatisticsDTO(
                            level.name(),
                            new Object[]{students.stream().filter(st -> st.getDetails().getEducationLevel().equals(level)).count()}
                    )
            );
        }
        return list;
    }

    @Override
    public List<StudentStatisticsDTO> getStudentHiredRatio() {
        List<Student> students = studentRepository.findAll();
        long studentsHiredCount = students.stream().filter(st -> st.getDetails().getAccountStatus().equals(StudentAccountStatus.HIRED)).count();
        long studentsExpelledCount = students.stream().filter(st -> st.getDetails().getAccountStatus().equals(StudentAccountStatus.EXPELLED)).count();
        long studentsTotalCount = students.stream().filter(st -> !st.getDetails().getAccountStatus().equals(StudentAccountStatus.HIRED) && !st.getDetails().getAccountStatus().equals(StudentAccountStatus.EXPELLED)).count();
        return List.of(
                new StudentStatisticsDTO(
                        StudentAccountStatus.HIRED.name(),
                        new Object[]{studentsHiredCount}
                ),
                new StudentStatisticsDTO(
                        StudentAccountStatus.EXPELLED.name(),
                        new Object[]{studentsExpelledCount}
                ),
                new StudentStatisticsDTO(
                        "TOTAL",
                        new Object[]{studentsTotalCount}
                )
        );
    }

    @Override
    public Page<StudentRatingTableDTO> getStudentsSortedByRating(int limit, Sort.Direction direction) {
        Pageable pageable = PageRequest.of(0, limit, direction, "rating");
        return studentRepository.findAll(pageable).map(s -> new StudentRatingTableDTO(
                s.getId(),
                s.getFullName(),
                s.getCourse().getId(),
                s.getCourse().getName(),
                s.getCourse().getIcon(),
                s.getRating(),
                lessonReportRowRepository.getAverageStudentLearningTime(s).orElse(0.0),
                s.getCreatedAt(),
                s.getLearningDuration(),
                s.getDetails().getAccountStatus()
        ));
    }

    @Override
    public Specification<Student> buildSpecificationFromFilters(FilterForm filters) {

        log.info("Building specification from filters: " + filters);

        Long studentId = filters.getId();
        String combined = filters.getCombined();
        String nameEmailInput = filters.getName();
        Long courseID = filters.getCourse();
        String phoneInput = filters.getPhone();
        String telegramInput = filters.getTelegram();
        Integer ratingInput = filters.getRating();
        Integer ratingFromInput = filters.getRatingFrom();
        Integer ratingToInput = filters.getRatingTo();
        String statusInput = filters.getStatus();

        Course course = NumericUtils.fieldIsEmpty(courseID) ? null : courseRepository.getReferenceById(courseID);
        StudentAccountStatus status = StringUtils.fieldIsEmpty(statusInput) ? null : StudentAccountStatus.valueOf(statusInput);

        Specification<Student> combinedSpec = Specification.where(StudentSpecifications.hasNameOrEmailLike(combined).or(StudentSpecifications.hasTelegramLike(combined)));

        Specification<Student> spec = Specification.where(combinedSpec
                                                    .and(StudentSpecifications.hasId(studentId))
                                                    .and(StudentSpecifications.hasCourse(course))
                                                    .and(StudentSpecifications.hasPhoneLike(phoneInput))
                                                    .and(StudentSpecifications.hasRatingBetween(ratingFromInput, ratingToInput))
//                                                    .and(StudentSpecifications.hasRatingOrHigher(ratingInput))
                                                    .and(StudentSpecifications.hasStatus(status)));

        return spec;
    }
}
