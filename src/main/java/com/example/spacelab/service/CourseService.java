package com.example.spacelab.service;

import com.example.spacelab.dto.course.CourseEditDTO;
import com.example.spacelab.dto.course.CourseIconDTO;
import com.example.spacelab.dto.course.StudentCourseTaskInfoDTO;
import com.example.spacelab.dto.statistics.CourseStatisticsDTO;
import com.example.spacelab.dto.task.TaskCourseDTO;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.util.FilterForm;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.List;
@Hidden
public interface CourseService extends EntityFilterService<Course> {

    List<Course> getCourses();
    Page<Course> getCourses(Pageable pageable);
    Page<Course> getCourses(FilterForm filters, Pageable pageable);

    // filter by allowed courses
    List<Course> getAllowedCourses(Long... ids);
    Page<Course> getAllowedCourses(Pageable pageable, Long... ids);
    Page<Course> getAllowedCourses(FilterForm filters, Pageable pageable, Long... ids);

    Page<Course> getCoursesByName(String name, Pageable pageable);

    Course getCourseById(Long id);
    Course createNewCourse(CourseEditDTO dto);
    Course editCourse(CourseEditDTO dto);

    void deleteCourseById(Long id);

    void saveIcon(Long id, CourseIconDTO dto) throws IOException;
    void deleteIcon(Long id) throws IOException;

    List<Task> getCourseTasks(Long id);

    StudentCourseTaskInfoDTO getStudentCourseInfo(Long studentID);

    void createTrackingCourseProject(Course course);
    void updateTrackingCourseProject(Course course);

    void removeAdminsFromCourse(Long courseId);

    List<Course> getAdminCourses(Long adminId);

    List<CourseStatisticsDTO> getCoursesByStudentRating(int limit, Sort.Direction direction);
    List<CourseStatisticsDTO> getCoursesByHiredStudents(int limit, Sort.Direction direction);
    List<CourseStatisticsDTO> getCoursesByDifficulty(int limit, Sort.Direction direction);
    List<CourseStatisticsDTO> getCoursesByLessonCount(int limit, Sort.Direction direction);

    long getActiveCoursesCount();
}
