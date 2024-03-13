package com.example.spacelab.api;

import com.example.spacelab.dto.student.StudentEditDTO;
import com.example.spacelab.dto.student.StudentInviteRequestDTO;
import com.example.spacelab.dto.student.StudentRegisterDTO;
import com.example.spacelab.util.FilterForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;

@Tag(name = "Student", description = "Student API")
public interface StudentAPI {

    /**
     * Get list of students paginated by page/size params
     * @param filters {@link FilterForm}, filter object
     * @param page {@link Integer}, default value 0
     * @param size {@link Integer}, default value 10
     * @return {@link ResponseEntity} representing the object
     */
    @Operation(summary = "Get Students", description = "Retrieve a paginated list of students based on provided filters, page, and size.")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('students.read.NO_ACCESS')")
    ResponseEntity<?> getStudents(FilterForm filters, Integer page, Integer size);

    /**
     * Get details of a student by their ID.
     * @param studentID {@link Long} representing the student's ID
     * @return {@link ResponseEntity} representing the object
     */
    @Operation(summary = "Get Student", description = "Retrieve details of a student by their ID.")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('students.read.NO_ACCESS')")
    ResponseEntity<?> getStudent(Long studentID);

    /**
     * Get the card information of a student by their ID.
     * @param studentID {@link Long} representing the student's ID
     * @return {@link ResponseEntity} representing the object
     */
    @Operation(summary = "Get Student Card", description = "Retrieve the card information of a student by their ID.")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('students.read.NO_ACCESS')")
    ResponseEntity<?> getStudentCard(Long studentID);

    /**
     * Get the lessons associated with a student by their ID.
     * @param studentID {@link Long} representing the student's ID
     * @return {@link ResponseEntity} representing the object
     */
    @Operation(summary = "Get Student Lessons", description = "Retrieve the lessons associated with a student by their ID.")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('students.read.NO_ACCESS')")
    ResponseEntity<?> getStudentLessons(Long studentID);

    /**
     * Get course information associated with a student by their ID.
     * @param studentID {@link Long} representing the student's ID
     * @return {@link ResponseEntity} representing the object
     */
    @Operation(summary = "Get Student Course Info", description = "Retrieve course information associated with a student by their ID.")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('students.read.NO_ACCESS')")
    ResponseEntity<?> getStudentCourseInfo(Long studentID);

    /**
     * Get lesson data associated with a student by their ID, along with pagination and filtering options.
     * @param studentID {@link Long} representing the student's ID
     * @param filters {@link FilterForm}, filter object
     * @param page {@link Integer}, default value 0
     * @param size {@link Integer}, default value 10
     * @return {@link ResponseEntity} representing the object
     */
    @Operation(summary = "Get Student Lesson Data", description = "Retrieve lesson data associated with a student by their ID, with pagination and filtering options.")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('students.read.NO_ACCESS')")
    ResponseEntity<?> getStudentLessonData(Long studentID, FilterForm filters, Integer page, Integer size);

    /**
     * Create a new student.
     * @param dto {@link StudentEditDTO} containing the student's details
     * @param bindingResult {@link BindingResult} for validation
     * @return {@link ResponseEntity} representing the object
     */
    @Operation(summary = "Create New Student", description = "Create a new student with the provided details.")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('students.write.NO_ACCESS')")
    ResponseEntity<?> createNewStudent(StudentEditDTO dto, BindingResult bindingResult);

    /**
     * Create an invite link for a student.
     * @param inviteRequest {@link StudentInviteRequestDTO} containing invite details
     * @param bindingResult {@link BindingResult} for validation
     * @param req {@link HttpServletRequest} for request details
     * @return {@link ResponseEntity} representing the object
     */
    @Operation(summary = "Create Student Invite Link", description = "Create an invite link for a student.")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('students.invite.NO_ACCESS')")
    ResponseEntity<?> createStudentInviteLink(StudentInviteRequestDTO inviteRequest, BindingResult bindingResult, HttpServletRequest req);

    /**
     * Register a new student.
     * @param dto {@link StudentRegisterDTO} containing the student's registration details
     * @param bindingResult {@link BindingResult} for validation
     * @return {@link ResponseEntity} representing the object
     */
    @Operation(summary = "Register Student", description = "Register a new student with the provided registration details.")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('students.write.NO_ACCESS')")
    ResponseEntity<?> registerStudent(StudentRegisterDTO dto, BindingResult bindingResult);

    /**
     * Load a student's details for editing.
     * @param id {@link Long} representing the student's ID
     * @return {@link ResponseEntity} representing the object
     */
    @Operation(summary = "Load Student For Edit", description = "Load a student's details for editing.")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('students.read.NO_ACCESS')")
    ResponseEntity<?> loadStudentForEdit(Long id);

    /**
     * Edit an existing student's details.
     * @param id {@link Long} representing the student's ID
     * @param dto {@link StudentEditDTO} containing the updated details
     * @param bindingResult {@link BindingResult} for validation
     * @return {@link ResponseEntity} representing the object
     */
    @Operation(summary = "Edit Student", description = "Edit an existing student's details.")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('students.edit.NO_ACCESS')")
    ResponseEntity<?> editStudent(Long id, StudentEditDTO dto, BindingResult bindingResult);

    /**
     * Delete a student by their ID.
     * @param id {@link Long} representing the student's ID
     * @return {@link ResponseEntity} representing the object
     */
    @Operation(summary = "Delete Student", description = "Delete a student by their ID.")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('students.delete.NO_ACCESS')")
    ResponseEntity<?> deleteStudent(Long id);

    /**
     * Get avatars of students with pagination.
     * @param page {@link Integer}, default value 0
     * @param size {@link Integer}, default value 0
     * @return {@link ResponseEntity} representing the object
     */
    @Operation(summary = "Get Student Avatars", description = "Get avatars of students with pagination.")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('students.read.NO_ACCESS')")
    ResponseEntity<?> getStudentAvatars(Integer page, Integer size);

    /**
     * Get students who aren't enrolled in any course with pagination and filtering options.
     * @param filters {@link FilterForm}, filter object
     * @param page {@link Integer}, default value 0
     * @param size {@link Integer}, default value 5
     * @return {@link ResponseEntity} representing the object
     */
    @Operation(summary = "Get Students Without Courses", description = "Get students who aren't enrolled in any course with pagination and filtering options.")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @PreAuthorize("!hasAuthority('students.read.NO_ACCESS')")
    ResponseEntity<?> getStudentsWithoutCourse(FilterForm filters, int page, int size);

    /**
     * Get a list of student account statuses.
     * @return {@link ResponseEntity} representing the object
     */
    @Operation(summary = "Get Status List", description = "Get a list of student account statuses.")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    ResponseEntity<?> getStatusList();
}
