package com.example.spacelab.controller;

import com.example.spacelab.integration.TaskTrackingService;
import com.example.spacelab.mapper.LessonMapper;
import com.example.spacelab.mapper.StudentMapper;
import com.example.spacelab.mapper.TaskMapper;
import com.example.spacelab.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@RestController
@Transactional
@RequiredArgsConstructor
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final CourseService courseService;
    private final StudentService studentService;
    private final TaskService taskService;
    private final LessonService lessonService;
    private final AdminService adminService;
    private final TaskTrackingService trackingService;
    private final StudentMapper studentMapper;
    private final TaskMapper taskMapper;
    private final LessonMapper lessonMapper;

    @GetMapping("/total-learning-time")
    public ResponseEntity<?> getPlatformTotalLearningTime() {
        return ResponseEntity.ok(Map.of(
                "total", trackingService.getTotalLearningTimeForPlatform(),
                "recent", trackingService.getRecentLearningTimeForPlatform()
        ));
    }

    @GetMapping("/student-total-learning-time")
    public ResponseEntity<?> getStudentTotalLearningTime(@RequestParam Long student) {
        return ResponseEntity.ok(Map.of(
                "total", studentService.getStudentTotalLearningTime(student),
                "recent", studentService.getStudentRecentLearningTime(student)
        ));
    }

    @GetMapping("/student-profile-graphs")
    public ResponseEntity<?> getStudentLearningTimeDistribution(@RequestParam Long student,
                                                                @RequestParam(required = false) String fromString,
                                                                @RequestParam(required = false) String toString) {
        LocalDateTime from = fromString != null && !fromString.isEmpty()
                ? LocalDate.parse(fromString, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay()
                : null;
        LocalDateTime to = toString != null && !toString.isEmpty()
                ? LocalDate.parse(toString, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atTime(LocalTime.MAX)
                : null;
        log.info("from: {}, to: {}", from, to);
        return ResponseEntity.ok(Map.of(
                "learningTime", studentService.getStudentLearningTimeDistribution(student, from, to),
                "rating", studentService.getStudentRatingDistribution(student, from, to)
        ));
    }

    @GetMapping("/student-task-cards")
    public ResponseEntity<?> getStudentTaskCards(@RequestParam Long student) {
        return ResponseEntity.ok(Map.of(
                "lastCompleted", studentService.getStudentLastCompletedTask(student).map(taskMapper::studentTaskToLinkDTO),
                "completedAmount", studentService.getStudentCompletedTaskAmount(student)
        ));
    }

    @GetMapping("/student-lesson-cards")
    public ResponseEntity<?> getStudentLessonCards(@RequestParam Long student) {
        return ResponseEntity.ok(Map.of(
                "lastVisitedLesson", studentService.getStudentLastVisitedLesson(student).map(lessonMapper::fromLessonToLinkDTO),
                "nextLesson", studentService.getStudentNextLesson(student).map(lessonMapper::fromLessonToLinkDTO),
                "visitedAmount", studentService.getStudentVisitedLessonAmount(student),
                "skippedAmount", studentService.getStudentSkippedLessonAmount(student)
        ));
    }

    @GetMapping("/active-students-count")
    public ResponseEntity<?> getActiveStudentsCount() {

        return ResponseEntity.ok().build();
    }
    @GetMapping("/active-courses-count")
    public ResponseEntity<?> getActiveCoursesCount() {

        return ResponseEntity.ok().build();
    }
    @GetMapping("/completed-lessons-count")
    public ResponseEntity<?> getCompletedLessonsCount() {

        return ResponseEntity.ok().build();
    }
    @GetMapping("/active-tasks-count")
    public ResponseEntity<?> getActiveTasksCount() {

        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin-panel-cards")
    public ResponseEntity<?> getAdminPanelCards() {
        return ResponseEntity.ok(Map.of(
                "activeStudents", studentService.getActiveStudentsCount(),
                "activeCourses", courseService.getActiveCoursesCount(),
                "activeTasks", taskService.getActiveTasksCount(),
                "completedLessons", lessonService.getCompletedLessonsCount(),
                "hiredStudents", studentService.getHiredStudentsCount(),
                "expelledStudents", studentService.getExpelledStudentsCount(),
                "totalStudents", studentService.getTotalStudentsCount()
        ));
    }

    @GetMapping("/courses-by-rating")
    public ResponseEntity<?> getCoursesSortedByRating(@RequestParam(defaultValue = "10") int limit,
                                                      @RequestParam(defaultValue = "ASC") Sort.Direction sort) {

        return ResponseEntity.ok(courseService.getCoursesByStudentRating(limit, sort));
    }

    @GetMapping("/courses-by-hired-students")
    public ResponseEntity<?> getCoursesSortedByHiredStudents(@RequestParam(defaultValue = "10") int limit,
                                                             @RequestParam(defaultValue = "ASC") Sort.Direction sort) {

        return ResponseEntity.ok(courseService.getCoursesByHiredStudents(limit, sort));
    }

    @GetMapping("/courses-by-difficulty")
    public ResponseEntity<?> getCoursesSortedByDifficulty(@RequestParam(defaultValue = "10") int limit,
                                                             @RequestParam(defaultValue = "ASC") Sort.Direction sort) {

        return ResponseEntity.ok(courseService.getCoursesByDifficulty(limit, sort));
    }

    @GetMapping("/courses-by-lesson-count")
    public ResponseEntity<?> getCoursesSortedByLessonCount(@RequestParam(defaultValue = "10") int limit,
                                                          @RequestParam(defaultValue = "ASC") Sort.Direction sort) {

        return ResponseEntity.ok(courseService.getCoursesByLessonCount(limit, sort));
    }

    @GetMapping("/courses-by-learning-time")
    public ResponseEntity<?> getCoursesByLearningTime() {

        return ResponseEntity.ok().build();
    }

    @GetMapping("/students-by-learning-time-total")
    public ResponseEntity<?> getStudentsByLearningTime() {

        return ResponseEntity.ok().build();
    }

    @GetMapping("/student-rating-timeline")
    public ResponseEntity<?> getStudentRatingByTime(@RequestParam Long[] studentIds) {

        return ResponseEntity.ok().build();
    }

    @GetMapping("/registered-students-timeline")
    public ResponseEntity<?> getRegisteredStudentsByTime(@RequestParam(required = false) ZonedDateTime from,
                                                         @RequestParam(required = false) ZonedDateTime to) {

        return ResponseEntity.ok().build();
    }

    // doughnut chart
    @GetMapping("/students-by-gender")
    public ResponseEntity<?> getStudentsDistributionByGender() {
        return ResponseEntity.ok(studentService.getStudentDistributionByGender());
    }

    // pie chart
    @GetMapping("/students-by-english")
    public ResponseEntity<?> getStudentsDistributionByEnglishLevel() {
        return ResponseEntity.ok(studentService.getStudentDistributionByEnglishLevel());
    }

    @GetMapping("/students-by-education")
    public ResponseEntity<?> getStudentsDistributionByEducationLevel() {
        return ResponseEntity.ok(studentService.getStudentDistributionByEducationLevel());
    }

    @GetMapping("/students-by-work-status")
    public ResponseEntity<?> getStudentsDistributionByWorkStatus() {
        return ResponseEntity.ok(studentService.getStudentDistributionByWorkStatus());
    }

    @GetMapping("/students-by-account-status")
    public ResponseEntity<?> getStudentsDistributionByAccountStatus() {
        return ResponseEntity.ok(studentService.getStudentDistributionByAccountStatus());
    }

    @GetMapping("/students-hired-ratio")
    public ResponseEntity<?> getStudentsHiredRatio() {
        return ResponseEntity.ok(studentService.getStudentHiredRatio());
    }

    @GetMapping("/admins-by-lessons")
    public ResponseEntity<?> getAdminsByCompletedLessons() {

        return ResponseEntity.ok().build();
    }

    // get 100 top students
    @GetMapping("/students-by-rating")
    public ResponseEntity<?> getStudentsSortedByRating(@RequestParam(defaultValue = "10") int limit,
                                                       @RequestParam(defaultValue = "ASC") Sort.Direction sort) {
        return ResponseEntity.ok(studentService.getStudentsSortedByRating(limit, sort));
    }

    @GetMapping("/student-rating")
    public ResponseEntity<?> getStudentAverageAndRecentRating(@RequestParam Long student) {
        return ResponseEntity.ok(Map.of(
                "total", studentService.getStudentTotalAverageRating(student),
                "recent", studentService.getStudentRecentAverageRating(student)
        ));
    }

}
