package com.example.spacelab.mapper;

import com.example.spacelab.dto.admin.AdminAvatarDTO;
import com.example.spacelab.dto.course.*;
import com.example.spacelab.dto.student.StudentAvatarDTO;
import com.example.spacelab.dto.task.TaskCourseDTO;
import com.example.spacelab.exception.MappingException;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.course.CourseInfo;
import com.example.spacelab.model.literature.Literature;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.repository.*;
import com.example.spacelab.util.ProgramDuration;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Data
@RequiredArgsConstructor
public class CourseMapper {

    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final TaskRepository taskRepository;
    private final LiteratureRepository literatureRepository;
    private final StudentMapper studentMapper;
    private final CourseRepository courseRepository;


    public CourseListDTO fromCourseToListDTO(Course course) {
        CourseListDTO dto = new CourseListDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setIcon(course.getIcon());
        dto.setStudentsQuantity((long) course.getStudents().size());
        dto.setBeginDate(course.getCourseInfo().getBeginDate());
        dto.setCompletionTime(course.getCourseInfo().getCompletionTime());
        if(course.getCourseInfo().getCompletionTimeUnit() != null) {
            dto.setCompletionTimeUnit(course.getCourseInfo().getCompletionTimeUnit().name());
        }

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
        CourseInfo courseInfo = course.getCourseInfo();
        dto.setDescription(courseInfo.getMain_description());
        dto.setTopics(courseInfo.getTopics());
        dto.setSettings(new CourseSettingsDTO(
                courseInfo.getBeginDate(),
                courseInfo.getCompletionTime(),
                courseInfo.getCompletionTimeUnit(),
                courseInfo.getGroupSize(),
                courseInfo.getHoursNorm(),
                courseInfo.getLessonInterval()
        ));
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
        List<Task> courseTasks = taskRepository.getCourseTasks(course.getId());
        if (courseTasks != null && !courseTasks.isEmpty()) {
            for (Task task : courseTasks) {
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
        course.setBeginningDate(courseDTO.getBeginDate());


        Admin mentor = adminRepository.findById(courseDTO.getMentorID()).orElse(null);
        if (mentor != null) {
            course.setMentor(mentor);
        }


        Admin manager = adminRepository.findById(courseDTO.getManagerID()).orElse(null);
        if (manager != null) {
            course.setManager(manager);
        }

        return course;
    }

    public Course fromSaveUpdatedDTOtoCourse(CourseSaveUpdatedDTO courseDTO) {
        Course course = new Course();
        course.setId(courseDTO.getId());
        course.setName(courseDTO.getName());
        course.setBeginningDate(courseDTO.getBeginDate());
        course.setEndDate(courseDTO.getEndDate());
        course.setStatus(courseDTO.getStatus());

        Admin mentor = adminRepository.findById(courseDTO.getMentorID()).orElse(null);
        if (mentor != null) {
            course.setMentor(mentor);
        }


        Admin manager = adminRepository.findById(courseDTO.getManagerID()).orElse(null);
        if (manager != null) {
            course.setManager(manager);
        }


        Set<Student> students = new HashSet<>();
        if (courseDTO.getStudents() != null && !courseDTO.getStudents().isEmpty()) {
            for (Long studentId : courseDTO.getStudents()) {
                students.add(studentRepository.findById(studentId).orElse(null));
            }
        }
        course.setStudents(students);


        Set<Task> tasks = new HashSet<>();
        if (courseDTO.getTasks() != null && !courseDTO.getTasks().isEmpty()) {
            for (Long taskId : courseDTO.getTasks()) {
                tasks.add(taskRepository.findById(taskId).orElse(null));
            }
        }
//        course.setTasks(tasks);

        Set <Literature > literature = new HashSet<>();
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


    public CourseEditDTO fromCourseToEditDTO(Course course) {
        CourseEditDTO dto = new CourseEditDTO();

        try {
            CourseInfo courseInfo = course.getCourseInfo();
            dto.setId(course.getId());
            dto.setName(course.getName());
            dto.setIcon(course.getIcon());
            dto.setStatus(course.getStatus());
            dto.setInfo(
                new CourseInfoDTO(
                        course.getCourseInfo().getMain_description(),
                        course.getCourseInfo().getTopics(),
                        new CourseSettingsDTO(
                                courseInfo.getBeginDate(),
                                courseInfo.getCompletionTime(),
                                courseInfo.getCompletionTimeUnit(),
                                courseInfo.getGroupSize(),
                                courseInfo.getHoursNorm(),
                                courseInfo.getLessonInterval()
                        )
                )
            );
            Admin courseMentor = course.getMentor();
            Admin courseManager = course.getManager();
            dto.setMembers(
                new CourseMembersDTO(
                        courseMentor != null ? new AdminAvatarDTO(
                                courseMentor.getId(),
                                courseMentor.getFullName(),
                                courseMentor.getAvatar()
                        ) : null,
                        courseManager != null ? new AdminAvatarDTO(
                                courseManager.getId(),
                                courseManager.getFullName(),
                                courseManager.getAvatar()
                        ) : null,
                        course.getStudents().stream().map(student -> new StudentAvatarDTO(
                                student.getId(),
                                student.getFullName(),
                                student.getAvatar()
                        )).toList()
                )
            );
            dto.setStructure(
                    new CourseTaskStructureDTO(
                            taskRepository.getCourseTasks(course.getId())
                                    .stream()
                                    .map(this::fromTaskToCourseDTO)
                                    .sorted(Comparator.comparing(TaskCourseDTO::getTaskIndex))
                                    .toList()
                    )
            );


        } catch (Exception e) {
            throw new MappingException(e.getMessage());
        }

        return dto;
    }

    public CourseInfoPageDTO fromCourseToInfoPageDTO(Course course) {
        CourseInfoPageDTO dto = new CourseInfoPageDTO();

        try {
            CourseInfo courseInfo = course.getCourseInfo();
            dto.setName(course.getName());
            dto.setIcon(course.getIcon());
            dto.setStatus(course.getStatus());
            dto.setInfo(
                    new CourseInfoDTO(
                            courseInfo.getMain_description(),
                            courseInfo.getTopics(),
                            new CourseSettingsDTO(
                                    courseInfo.getBeginDate(),
                                    courseInfo.getCompletionTime(),
                                    courseInfo.getCompletionTimeUnit(),
                                    courseInfo.getGroupSize(),
                                    courseInfo.getHoursNorm(),
                                    courseInfo.getLessonInterval()
                            )
                    )
            );
            Admin courseMentor = course.getMentor();
            Admin courseManager = course.getManager();
            dto.setMembers(
                    new CourseMembersDTO(
                            courseMentor != null ? new AdminAvatarDTO(
                                    courseMentor.getId(),
                                    courseMentor.getFullName(),
                                    courseMentor.getAvatar()
                            ) : null,
                            courseManager != null ? new AdminAvatarDTO(
                                    courseManager.getId(),
                                    courseManager.getFullName(),
                                    courseManager.getAvatar()
                            ) : null,
                            course.getStudents().stream().map(student -> new StudentAvatarDTO(
                                    student.getId(),
                                    student.getFullName(),
                                    student.getAvatar()
                            )).toList()
                    )
            );
            dto.setStructure(
                    new CourseTaskStructureDTO(
                            taskRepository.getCourseTasks(course.getId())
                                    .stream()
                                    .map(this::fromTaskToCourseDTO)
                                    .sorted(Comparator.comparing(TaskCourseDTO::getTaskIndex))
                                    .toList()
                    )
            );

        } catch (Exception e) {
            throw new MappingException(e.getMessage());
        }

        return dto;
    }

    public Course fromEditDTOToCourse(CourseEditDTO dto) {
        Course course = (dto.getId() != null) ? courseRepository.findById(dto.getId()).orElse(new Course()) : new Course();

        try {
            course.setName(dto.getName());
            if(dto.getStatus() != null) course.setStatus(dto.getStatus());
            CourseInfo courseInfo = course.getCourseInfo();

            courseInfo.setMain_description(dto.getInfo().getDescription());
            courseInfo.getTopics().clear();
            courseInfo.getTopics().addAll(dto.getInfo().getTopics());
            courseInfo.setGroupSize(dto.getInfo().getSettings().groupSize());
            courseInfo.setHoursNorm(dto.getInfo().getSettings().hoursNorm());
            courseInfo.setCompletionTime(dto.getInfo().getSettings().completionTime());
            courseInfo.setCompletionTimeUnit(dto.getInfo().getSettings().completionTimeUnit());
            courseInfo.setLessonInterval(dto.getInfo().getSettings().lessonInterval());
            courseInfo.setBeginDate(dto.getInfo().getSettings().beginDate());

            AdminAvatarDTO mentor = dto.getMembers().getMentor();
            AdminAvatarDTO manager = dto.getMembers().getManager();
            if(mentor != null) course.setMentor(adminRepository.getReferenceById(mentor.getId()));
            if(manager != null) course.setManager(adminRepository.getReferenceById(manager.getId()));

        } catch (Exception e) {
            throw new MappingException(e.getMessage());
        }

        return course;
    }

    public TaskCourseDTO fromTaskToCourseDTO(Task task) {
        return new TaskCourseDTO(
                task.getId(),
                task.getTaskIndex(),
                task.getName(),
                task.getSubtasks().stream().map(this::fromTaskToCourseDTO).toList()
        );
    }

}
