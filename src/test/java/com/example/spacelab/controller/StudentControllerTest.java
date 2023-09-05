package com.example.spacelab.controller;

import com.example.spacelab.config.SecurityConfig;
import com.example.spacelab.dto.student.*;
import com.example.spacelab.mapper.StudentMapper;
import com.example.spacelab.mapper.TaskMapper;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.lesson.LessonReportRow;
import com.example.spacelab.model.role.UserRole;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.student.StudentDetails;
import com.example.spacelab.model.student.StudentInviteRequest;
import com.example.spacelab.model.student.StudentTask;
import com.example.spacelab.service.StudentService;
import com.example.spacelab.validator.StudentValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@WithUserDetails("admin@gmail.com")
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService service;

    @MockBean
    private StudentMapper mapper;

    @MockBean
    private TaskMapper taskMapper;

    @Autowired
    private StudentValidator validator;

    static Student student;
    static List<Student> studentList;
    static Page<Student> studentPage;
    static StudentDTO dto;
    static List<StudentDTO> dtoList;

    static StudentEditDTO editDTO;

    static StudentTask task;
    static List<StudentTask> taskList;
    static StudentTaskDTO taskDTO;

    static StudentCardDTO cardDTO;

    static LessonReportRow row;

    static ObjectMapper objectMapper = new JsonMapper();

    @BeforeAll
    public static void setupObjects() {
        student = new Student();
        student.setAvatar("test");
        student.setCourse(new Course());
        student.setRating(10);
        student.setRole(new UserRole());
        student.setPassword("test");
        student.setId(1L);
        student.setDetails(new StudentDetails());
        student.setLessonData(new ArrayList<>());
        student.setTasks(new ArrayList<>());

        studentList = List.of(student, student, student);
        studentPage = new PageImpl<>(studentList);

        dto = new StudentDTO();

        dtoList = List.of(dto, dto, dto);

        editDTO = new StudentEditDTO(1L, "test", "test",
                "test", 1L, "test@gmail.com", "+380997524927", "@test");

        task = new StudentTask();
        task.setStudent(student);
        task.setId(1L);

        taskList = List.of(task, task, task);

        taskDTO = new StudentTaskDTO();

        cardDTO = new StudentCardDTO();

    }

    @BeforeEach
    public void setupMocks() {
        when(service.getStudents()).thenReturn(studentList);
        when(service.getStudents(any(), any())).thenReturn(studentPage);
        when(service.getStudentCourseID(anyLong())).thenReturn(1L);
        when(service.getStudentById(anyLong())).thenReturn(student);

        when(service.getStudentTasks(anyLong())).thenReturn(taskList);
        when(service.getStudentTasks(any(), any())).thenReturn(taskList);
        when(service.getStudentTask(anyLong())).thenReturn(task);

        when(service.getCard(anyLong())).thenReturn(cardDTO);

        when(service.createNewStudent(any())).thenReturn(student);
        when(service.editStudent(any())).thenReturn(student);

        when(service.createInviteStudentToken(any())).thenReturn("test");

        when(mapper.fromStudentToDTO(any())).thenReturn(dto);
        when(mapper.fromEditDTOToStudent(any())).thenReturn(student);
        when(mapper.fromStudentToAvatarDTO(any())).thenReturn(new StudentAvatarDTO());
        when(taskMapper.fromStudentTaskToDTO(any())).thenReturn(taskDTO);
    }

    @Test
    void getStudents() throws Exception {

        MvcResult result = mockMvc.perform(get("/api/students")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

    }

    @Test
    void getStudent() throws Exception {

        Long id = 1L;

        MvcResult result = mockMvc.perform(get("/api/students/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void getStudentTasks() throws Exception  {

        Long id = 1L;

        MvcResult result = mockMvc.perform(get("/api/students/{id}/tasks", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    void getStudentTask() throws Exception {

        Long id = 1L;

        MvcResult result = mockMvc.perform(get("/api/students/{studentID}/tasks/{taskID}", id, id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    void getStudentCard() throws Exception {

        Long id = 1L;

        MvcResult result = mockMvc.perform(get("/api/students/{id}/card", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    void getStudentLessons() throws Exception {

        Long id = 1L;

        MvcResult result = mockMvc.perform(get("/api/students/{id}/lessons", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    void createNewStudent() throws Exception {

        MvcResult result = mockMvc.perform(post("/api/students")
                        .content(objectMapper.writeValueAsString(editDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

    }

    @Test
    void createStudentInviteLink() throws Exception {

        StudentInviteRequest request = new StudentInviteRequest();
        Course course = new Course();
        course.setId(1L);
        request.setCourse(course);

        MvcResult result = mockMvc.perform(post("/api/students/invite")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andExpect(content().string("http://localhost:80/register/test"))
                        .andReturn();
    }

    @Test
    void registerStudent() throws Exception {
    }

    @Test
    void editStudent() throws Exception {

        Long id = 1L;

        MvcResult result = mockMvc.perform(put("/api/students/{id}", id)
                        .content(objectMapper.writeValueAsString(editDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

    }

    @Test
    void deleteStudent() throws Exception {

        Long id = 1L;

        MvcResult result = mockMvc.perform(delete("/api/students/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andReturn();

    }

    @Test
    void getStudentAvatars() throws Exception {

        MvcResult result = mockMvc.perform(get("/api/students/get-student-avatars")
                                    .contentType(MediaType.APPLICATION_JSON))
                                    .andExpect(status().isOk())
                                    .andReturn();

    }

    @Test
    void getStudentsWithoutCourse() throws Exception {

        MvcResult result = mockMvc.perform(get("/api/students/get-available-students")
                                            .contentType(MediaType.APPLICATION_JSON))
                                            .andExpect(status().isOk())
                                            .andReturn();

    }
}