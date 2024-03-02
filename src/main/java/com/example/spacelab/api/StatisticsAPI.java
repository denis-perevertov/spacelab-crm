package com.example.spacelab.api;

import com.example.spacelab.api.example.*;
import com.example.spacelab.dto.statistics.StudentRatingTableDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Statistics", description = "Statistics API")
public interface StatisticsAPI {

    @Operation(
            summary = "Get Total Learning Time Across Platform",
            description = "Get total hours and minutes spent on learning, for all students, on all projects"
    )
    @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(schema = @Schema(implementation = StudentLearningTimeExample.class)))
    ResponseEntity<?> getPlatformTotalLearningTime();

    @Operation(
            summary = "Get Total Learning Time For Student",
            description = "Get total hours and minutes spent on learning, for student with specified ID, on all his projects"
    )
    @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(schema = @Schema(implementation = StudentLearningTimeExample.class)))
    ResponseEntity<?> getStudentTotalLearningTime(@Parameter(required = true, example = "1") Long student);

    @Operation(
            summary = "Get Student Learning Time / Rating Distribution",
            description = "Student learning time / rating distribution for graph"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(schema = @Schema(implementation = StudentTimeRatingDistributionExample.class)))
    })
    ResponseEntity<?> getStudentLearningTimeDistribution(@RequestParam Long student,
                                                         @RequestParam(required = false) String fromString,
                                                         @RequestParam(required = false) String toString);

    @Operation(
            summary = "Get Student Task Cards",
            description = "Last completed task, completed amount"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(schema = @Schema(implementation = StudentTaskCardsExample.class)))
    })
    ResponseEntity<?> getStudentTaskCards(@RequestParam Long student);

    @Operation(
            summary = "Get Student Lesson Cards",
            description = "Last visited / next lesson, visited/skipped amount"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(schema = @Schema(implementation = StudentLessonCardsExample.class)))
    })
    ResponseEntity<?> getStudentLessonCards(@RequestParam Long student);

    @Operation(
            summary = "Get Active Students Count",
            description = "Get active students count"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    ResponseEntity<?> getActiveStudentsCount();

    @Operation(
            summary = "Get Active Courses Count",
            description = "Get active courses count"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    ResponseEntity<?> getActiveCoursesCount();

    @Operation(
            summary = "Get Completed Lessons Count",
            description = "Get completed lessons count"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    ResponseEntity<?> getCompletedLessonsCount();

    @Operation(
            summary = "Get Active Tasks Count",
            description = "Get active tasks count"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    ResponseEntity<?> getActiveTasksCount();

    @Operation(
            summary = "Get Hired Students Count",
            description = "Get hired students count"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    ResponseEntity<?> getHiredStudentsCount();

    @Operation(
            summary = "Get Expelled Students Count",
            description = "Get expelled students count"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    ResponseEntity<?> getExpelledStudentsCount();

    @Operation(
            summary = "Get Total Students Count",
            description = "Get total students count"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    ResponseEntity<?> getTotalStudentsCount();

    @Operation(
            summary = "Get Admin Panel Statistic Cards",
            description = "Active/expelled/hired students, active courses, active tasks, completed lessons etc."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(schema = @Schema(implementation = AdminPanelCardsExample.class)))
    })
    ResponseEntity<?> getAdminPanelCards();

    @Operation(
            summary = "Get Courses Sorted By Rating",
            description = """
                    Courses sorted by average ratings of their students
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(schema = @Schema(implementation = CourseStatisticDTOExample.class)))
    })
    ResponseEntity<?> getCoursesSortedByRating(@RequestParam(defaultValue = "10") int limit,
                                               @RequestParam(defaultValue = "ASC") Sort.Direction sort);

    @Operation(
            summary = "Get Courses Sorted By Hired Students Count",
            description = """
                    Courses sorted by amount of students hired from them
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(schema = @Schema(implementation = CourseStatisticDTOExample.class)))
    })
    ResponseEntity<?> getCoursesSortedByHiredStudents(@RequestParam(defaultValue = "10") int limit,
                                                      @RequestParam(defaultValue = "ASC") Sort.Direction sort);

    @Operation(
            summary = "Get Courses Sorted By Difficulty",
            description = """
                    Courses sorted by their difficulty level: sum of points for every task.
                    difficult task = 3 points
                    intermediate task = 2 points
                    beginner task = 1 points
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(schema = @Schema(implementation = CourseStatisticDTOExample.class)))
    })
    ResponseEntity<?> getCoursesSortedByDifficulty(@RequestParam(defaultValue = "10") int limit,
                                                   @RequestParam(defaultValue = "ASC") Sort.Direction sort);

    @Operation(
            summary = "Get Courses Sorted By Lesson Count",
            description = """
                    Courses sorted by amount of completed lessons
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(schema = @Schema(implementation = CourseStatisticDTOExample.class)))
    })
    ResponseEntity<?> getCoursesSortedByLessonCount(@RequestParam(defaultValue = "10") int limit,
                                                    @RequestParam(defaultValue = "ASC") Sort.Direction sort);


//    ResponseEntity<?> getCoursesByLearningTime();
//    ResponseEntity<?> getStudentsByLearningTime();
//
//    ResponseEntity<?> getStudentRatingByTime(@RequestParam Long[] studentIds);

//    ResponseEntity<?> getRegisteredStudentsByTime(@RequestParam(required = false) ZonedDateTime from,
//                                                  @RequestParam(required = false) ZonedDateTime to);

    @Operation(
            summary = "Get Student Distribution By Gender",
            description = """
                    Student distribution by gender : m / f / ...
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(schema = @Schema(implementation = CourseStatisticDTOExample.class)))
    })
    ResponseEntity<?> getStudentsDistributionByGender();

    @Operation(
            summary = "Get Student Distribution By English Level",
            description = """
                    Student distribution by english level : c2 / c1 / ...
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(schema = @Schema(implementation = CourseStatisticDTOExample.class)))
    })
    ResponseEntity<?> getStudentsDistributionByEnglishLevel();

    @Operation(
            summary = "Get Student Distribution By Education level",
            description = """
                    Student distribution by education level : highschool / bachelor / master / other
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(schema = @Schema(implementation = CourseStatisticDTOExample.class)))
    })
    ResponseEntity<?> getStudentsDistributionByEducationLevel();

    @Operation(
            summary = "Get Student Distribution By Work status",
            description = """
                    Student distribution by work status : student / full-time / part-time / ...
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(schema = @Schema(implementation = CourseStatisticDTOExample.class)))
    })
    ResponseEntity<?> getStudentsDistributionByWorkStatus();

    @Operation(
            summary = "Get Student Distribution By Account status",
            description = """
                    Student distribution by account status : active / inactive / expelled / ...
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(schema = @Schema(implementation = CourseStatisticDTOExample.class)))
    })
    ResponseEntity<?> getStudentsDistributionByAccountStatus();

    @Operation(
            summary = "Get Student Hired Ratio",
            description = """
                    Get ratio of hired students/total students
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(schema = @Schema(implementation = CourseStatisticDTOExample.class)))
    })
    ResponseEntity<?> getStudentsHiredRatio();

//    ResponseEntity<?> getAdminsByCompletedLessons();

    @Operation(
            summary = "Get Top Students Sorted By Rating",
            description = """
                    Get N-top students by rating sorted in ASC/DESC order, specified by parameters
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(array = @ArraySchema(minItems = 2, schema = @Schema(implementation = StudentRatingTableDTO.class))))
    })
    ResponseEntity<?> getStudentsSortedByRating(@RequestParam(defaultValue = "10") int limit,
                                                @RequestParam(defaultValue = "ASC") Sort.Direction sort);

    @Operation(
            summary = "Get Students Average Rating",
            description = """
                    Get average rating for all students (for all time & recently, in this month)
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched", content = @Content(schema = @Schema(implementation = StudentRatingStatisticExample.class)))
    })
    ResponseEntity<?> getStudentAverageAndRecentRating(@RequestParam Long student);


}
