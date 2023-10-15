//package com.example.spacelab.controller;
//
//
//import com.example.spacelab.config.JwtService;
//import com.example.spacelab.config.SecurityConfig;
//import com.example.spacelab.config.TestSecurityConfig;
//import com.example.spacelab.config.WithMockCustomUser;
//import com.example.spacelab.dto.course.*;
//import com.example.spacelab.mapper.CourseMapper;
//import com.example.spacelab.model.course.Course;
//import com.example.spacelab.service.CourseService;
//import com.example.spacelab.util.AuthUtil;
//import com.example.spacelab.util.FilterForm;
//import com.example.spacelab.validator.CourseValidator;
//import com.example.spacelab.validator.CourseUpdateValidator;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.security.test.context.support.WithUserDetails;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//
//import java.util.Arrays;
//import java.util.Collections;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Import(SecurityConfig.class)
//@WithUserDetails("admin@gmail.com")
//class CourseControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private CourseController controller;
//
//    @MockBean
//    private CourseService courseService;
//
//    @MockBean
//    private CourseMapper courseMapper;
//
//    @MockBean
//    private CourseValidator courseCreateValidator;
//
//    @MockBean
//    private CourseUpdateValidator courseUpdateValidator;
//
//    @MockBean
//    private AuthenticationManager authenticationManager;
//
//    @MockBean
//    private JwtService jwtService;
//
//    @Test
////    @WithMockUser(username = "test@gmail.com", password = "test", authorities = {"test"})
//    public void testGetCourses() throws Exception {
//        // Arrange
//        FilterForm filters = new FilterForm();
//        Page<Course> coursePage = new PageImpl<>(Arrays.asList(new Course(1L, "Sample Course")));
//        Page<CourseListDTO> courseListDTOPage = new PageImpl<>(Collections.singletonList(new CourseListDTO(1L, "Sample Course")));
//
//        when(courseService.getCourses()).thenReturn(coursePage.getContent());
//        when(courseService.getCourses(any(), any())).thenReturn(coursePage);
//        when(courseMapper.fromCoursePageToListDTOPage(any())).thenReturn(courseListDTOPage);
//
//        MvcResult result = mockMvc.perform(get("/api/courses")).andReturn();
//        System.out.println(result.getResponse().getContentAsString());
//
//        // Act and Assert
//        mockMvc.perform(get("/api/courses"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content[0].id").value(1))
//                .andExpect(jsonPath("$.content[0].name").value("Sample Course"));
//    }
//
//    @Test
//    @WithMockUser(username = "user", authorities = {"course.read.ACCESS"})
//    public void testGetCourse() throws Exception {
//        // Arrange
//        Long courseId = 1L;
//        CourseInfoDTO courseInfoDTO = new CourseInfoDTO(1L, "Sample Course");
//
//        when(courseService.getCourseById(courseId)).thenReturn(new Course(1L, "Sample Course"));
//        when(courseMapper.fromCourseToInfoDTO(any())).thenReturn(courseInfoDTO);
//
//        // Act and Assert
//        mockMvc.perform(get("/api/courses/{id}", courseId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.name").value("Sample Course"));
//    }
//
//    @Test
//    @WithMockUser(username = "user", authorities = {"course.read.ACCESS"})
//    public void testGetCourseForUpdate() throws Exception {
//        // Arrange
//        Long courseId = 1L;
//        CourseCardDTO courseCardDTO = new CourseCardDTO(1L, "Sample Course");
//
//        when(courseService.getCourseById(courseId)).thenReturn(new Course(1L, "Sample Course"));
//        when(courseMapper.fromCardDTOtoCourse(any())).thenReturn(courseCardDTO);
//
//        // Act and Assert
//        mockMvc.perform(get("/api/courses/update/{id}", courseId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.name").value("Sample Course"));
//    }
//
//    @Test
//    @WithMockUser(username = "user", authorities = {"course.write.ACCESS"})
//    public void testCreateNewCourse() throws Exception {
//        // Arrange
//        CourseSaveCreatedDTO createDTO = new CourseSaveCreatedDTO(1L, "Sample Course");
//
//        when(courseMapper.fromSaveCreatedDTOtoCourse(any())).thenReturn(new Course());
//
//        // Act and Assert
//        mockMvc.perform(post("/api/courses")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(createDTO)))
//                .andExpect(status().isCreated())
//                .andExpect(content().string("Course created"));
//    }
//
//    @Test
//    @WithMockUser(username = "user", authorities = {"course.edit.ACCESS"})
//    public void testUpdateCourse() throws Exception {
//        // Arrange
//        Long courseId = 1L;
//        CourseSaveUpdatedDTO updateDTO = new CourseSaveUpdatedDTO(1L, "Updated Course");
//
//        when(courseMapper.fromSaveUpdatedDTOtoCourse(any())).thenReturn(new Course());
//
//        // Act and Assert
//        mockMvc.perform(put("/api/courses/{id}", courseId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(updateDTO)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Course updated"));
//    }
//
//    @Test
//    @WithMockUser(username = "user", authorities = {"course.delete.ACCESS"})
//    public void testDeleteCourse() throws Exception {
//        // Arrange
//        Long courseId = 1L;
//
//        // Act and Assert
//        mockMvc.perform(delete("/api/courses/{id}", courseId))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Course deleted"));
//    }
//
//    @Test
//    @WithMockUser(username = "user", authorities = {"course.read.ACCESS"})
//    public void testGetCoursesForSelect2() throws Exception {
//        // Arrange
//        Page<CourseSelectDTO> courseSelectDTOPage = new PageImpl<>(Collections.singletonList(new CourseSelectDTO(1L, "Sample Course")));
//
//        when(courseService.getCoursesByName(anyString(), any())).thenReturn(new PageImpl<>(Collections.singletonList(new Course(1L, "Sample Course"))));
//        when(courseMapper.fromCoursePageToSelectDTOPage(any())).thenReturn(courseSelectDTOPage);
//
//        // Act and Assert
//        mockMvc.perform(get("/api/courses/getCourses")
//                        .param("searchQuery", "sample")
//                        .param("page", "0")
//                        .param("size", "10"))
//                .andExpect(status().isOk());
//    }
//
//    private String asJsonString(final Object obj) {
//        try {
//            return new ObjectMapper().writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//}