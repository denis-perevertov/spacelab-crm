package com.example.spacelab.mapper;

import com.example.spacelab.dto.course.*;
import com.example.spacelab.dto.student.StudentAvatarDTO;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.course.CourseInfo;
import com.example.spacelab.model.literature.Literature;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.repository.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Data
@RequiredArgsConstructor
public class CourseMapper {

    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final TaskRepository taskRepository;
    private final LiteratureRepository literatureRepository;
    private final StudentMapper studentMapper;


    public CourseListDTO fromCourseToListDTO(Course course) {
        CourseListDTO dto = new CourseListDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setStudentsQuantity((long) course.getStudents().size());
        dto.setBegin_date(course.getBeginningDate());
        dto.setEnd_date(course.getEndDate());

        Admin mentor = course.getMentor();
        if (mentor != null) {
            dto.setMentorId(mentor.getId());
            dto.setMentorName(mentor.getFirstName()+" "+mentor.getLastName());
        }

        Admin manager = course.getManager();
        if (manager != null) {
            dto.setManagerId(manager.getId());
            dto.setManagerName(manager.getFirstName()+" "+manager.getLastName());
        }

        dto.setStatus(course.getStatus());

        return dto;
    }

    public CourseInformationDTO fromCourseInfoToCourseInformationDTO(CourseInfo courseInfo) {
        CourseInformationDTO dto = new CourseInformationDTO();
        dto.setMain_description(courseInfo.getMain_description());
        dto.setTopics(courseInfo.getTopics());
        dto.setCompletionTime(courseInfo.getCompletionTime());
        dto.setGroupSize(courseInfo.getGroupSize());
        dto.setHoursNorm(courseInfo.getHoursNorm());
        return dto;
    }

    public CourseSelectDTO fromCourseToSelectDTO(Course course) {
        CourseSelectDTO dto = new CourseSelectDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        return dto;
    }

    public List <CourseListDTO> fromCourseListToListDTO(List<Course> courses) {
        List <CourseListDTO> dtos = new ArrayList<>();
        for (Course course : courses) {
            dtos.add(fromCourseToListDTO(course));
        }
        return dtos;
    }
    public Page<CourseListDTO> fromCoursePageToListDTOPage(Page<Course> coursePage) {
        List<CourseListDTO> dtos = new ArrayList<>();
        for (Course course : coursePage.getContent()) {
            dtos.add(fromCourseToListDTO(course));
        }
        return new PageImpl<>(dtos, coursePage.getPageable(), coursePage.getTotalElements());
    }

    public Page<CourseSelectDTO> fromCoursePageToSelectDTOPage(Page<Course> coursePage) {
        List<CourseSelectDTO> dtos = new ArrayList<>();
        for (Course course : coursePage.getContent()) {
            dtos.add(fromCourseToSelectDTO(course));
        }
        return new PageImpl<>(dtos, coursePage.getPageable(), coursePage.getTotalElements());
    }
    public CourseInfoDTO fromCourseToInfoDTO(Course course) {
        CourseInfoDTO dto = new CourseInfoDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setMain_description(course.getCourseInfo().getMain_description());
        dto.setTopics(course.getCourseInfo().getTopics());
        dto.setCompletionTime(course.getCourseInfo().getCompletionTime());
        dto.setGroupSize(course.getCourseInfo().getGroupSize());
        dto.setStatus(course.getStatus());
        return dto;
    }


    public CourseCardDTO fromCardDTOtoCourse(Course course) {
        CourseCardDTO dto = new CourseCardDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setBeginningDate(course.getBeginningDate());
        dto.setEndDate(course.getEndDate());

        Admin mentor = course.getMentor();
        if (mentor != null) {
            dto.setMentorId(mentor.getId());
            dto.setMentorName(mentor.getFirstName()+" "+mentor.getLastName()); // Assuming Admin has a 'name' property
        }

        Admin manager = course.getManager();
        if (manager != null) {
            dto.setManagerId(manager.getId());
            dto.setManagerName(manager.getFirstName()+" "+manager.getLastName()); // Assuming Admin has a 'name' property
        }

        List<StudentAvatarDTO> students = new ArrayList<>();
        if (course.getStudents() != null && !course.getStudents().isEmpty()) {
            for (Student student : course.getStudents()) {
                students.add(studentMapper.fromStudentToAvatarDTO(student));
            }
        }
        dto.setStudents(students);

        Map<Long, String> tasks = new HashMap<>();
        if (course.getTasks() != null && !course.getTasks().isEmpty()) {
            for (Task task : course.getTasks()) {
                tasks.put(task.getId(), task.getName());
            }
        }
        dto.setTasks(tasks);

        dto.setStatus(course.getStatus());
        CourseInformationDTO courseInfo;
        if (course.getCourseInfo() == null) {
            courseInfo = new CourseInformationDTO();

        } else {
            courseInfo = fromCourseInfoToCourseInformationDTO(course.getCourseInfo());
        }
        dto.setCourseInfo(courseInfo);

//        Map<Long, String> manegers = new HashMap<>();
//        List<Admin> admins = adminRepository.findAll();
//        if (admins != null && !admins.isEmpty()) {
//            for (Admin admin : admins) {
//                students.put(admin.getId(), admin.getFirstName()+" "+admin.getLastName());
//            }
//        }
//        dto.setManegers(manegers);
//
//        //Если будут фиксированные роли, можно будет получать отдельно менторов и админов.
//        dto.setMentors(manegers);

        return dto;
    }

    public Course fromSaveCreatedDTOtoCourse(CourseSaveCreatedDTO courseDTO) {
        Course course = new Course();
        course.setId(courseDTO.getId());
        course.setName(courseDTO.getName());
        course.setBeginningDate(courseDTO.getBeginningDate());


        Admin mentor = adminRepository.findById(courseDTO.getMentorId()).orElse(null);
        if (mentor != null) {
            course.setMentor(mentor);
        }


        Admin manager = adminRepository.findById(courseDTO.getManagerId()).orElse(null);
        if (manager != null) {
            course.setManager(manager);
        }

        return course;
    }

    public Course fromSaveUpdatedDTOtoCourse(CourseSaveUpdatedDTO courseDTO) {
        Course course = new Course();
        course.setId(courseDTO.getId());
        course.setName(courseDTO.getName());
        course.setBeginningDate(courseDTO.getBeginningDate());
        course.setEndDate(courseDTO.getEndDate());
        course.setStatus(courseDTO.getStatus());

        Admin mentor = adminRepository.findById(courseDTO.getMentorId()).orElse(null);
        if (mentor != null) {
            course.setMentor(mentor);
        }


        Admin manager = adminRepository.findById(courseDTO.getManagerId()).orElse(null);
        if (manager != null) {
            course.setManager(manager);
        }


        List<Student> students = new ArrayList<>();
        if (courseDTO.getStudents() != null && !courseDTO.getStudents().isEmpty()) {
            for (Long studentId : courseDTO.getStudents()) {
                students.add(studentRepository.findById(studentId).orElse(null));
            }
        }
        course.setStudents(students);


        List<Task> tasks = new ArrayList<>();
        if (courseDTO.getTasks() != null && !courseDTO.getTasks().isEmpty()) {
            for (Long taskId : courseDTO.getTasks()) {
                tasks.add(taskRepository.findById(taskId).orElse(null));
            }
        }
        course.setTasks(tasks);

        List <Literature > literature = new ArrayList<>();
        if (courseDTO.getLiterature() != null && !courseDTO.getLiterature().isEmpty()) {
            for (Long literatureId : courseDTO.getLiterature()) {
                literature.add(literatureRepository.findById(literatureId).orElse(null));
            }
        }
        course.setLiterature(literature);

        if (courseDTO.getCourseInfo().getMain_description() != null ||
                courseDTO.getCourseInfo().getTopics() != null ||
                courseDTO.getCourseInfo().getCompletionTime() != null ||
                courseDTO.getCourseInfo().getGroupSize() != null) {

            CourseInfo courseInfo;
            if (course.getCourseInfo() != null) {
                courseInfo = course.getCourseInfo();
            } else {
                courseInfo = new CourseInfo();
            }

            courseInfo.setMain_description(courseDTO.getCourseInfo().getMain_description());
            courseInfo.setTopics(courseDTO.getCourseInfo().getTopics());
            courseInfo.setCompletionTime(courseDTO.getCourseInfo().getCompletionTime());
            courseInfo.setGroupSize(courseDTO.getCourseInfo().getGroupSize());
            course.setCourseInfo(courseInfo);
        }
    return course;
    }

}
