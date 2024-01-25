package com.example.spacelab;

import com.example.spacelab.job.LessonMonitor;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.contact.ContactInfo;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.course.CourseInfo;
import com.example.spacelab.model.course.CourseStatus;
import com.example.spacelab.model.lesson.Lesson;
import com.example.spacelab.model.lesson.LessonReport;
import com.example.spacelab.model.lesson.LessonStatus;
import com.example.spacelab.model.literature.Literature;
import com.example.spacelab.model.literature.LiteratureType;
import com.example.spacelab.model.role.PermissionSet;
import com.example.spacelab.model.role.PermissionType;
import com.example.spacelab.model.role.UserRole;
import com.example.spacelab.model.student.*;
import com.example.spacelab.model.task.CompletionTime;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.model.task.TaskLevel;
import com.example.spacelab.model.task.TaskStatus;
import com.example.spacelab.repository.*;
import com.example.spacelab.service.LessonService;
import com.example.spacelab.service.specification.LessonSpecifications;
import com.example.spacelab.util.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Log
@Transactional
@RequiredArgsConstructor
public class DefaultInitializer implements CommandLineRunner {

    private final UserRoleRepository roleRepository;
    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final ContactInfoRepository contactRepository;
    private final CourseRepository courseRepository;
    private final TaskRepository taskRepository;
    private final LiteratureRepository literatureRepository;
    private final LessonRepository lessonRepository;

    private final PasswordEncoder passwordEncoder;

    private final LessonService lessonService;



    @Override
    public void run(String... args) throws InterruptedException {


        Thread.sleep(50); checkForRoles();
        Thread.sleep(50); checkForAdmins();
        Thread.sleep(50); checkForCourses();
        Thread.sleep(50); checkForContacts();
        Thread.sleep(50); checkForStudents();
        Thread.sleep(50); checkForTasks();
        Thread.sleep(50); checkForLiterature();
        Thread.sleep(50); checkForLessons();
    }


    private void checkForRoles() {
        if(roleRepository.count() < 1) {
            log.info("Adding default roles");

            UserRole adminRole = new UserRole();
            adminRole.setName("ADMIN");
            PermissionSet permissions = adminRole.getPermissions();
            permissions.setReadStatistics(PermissionType.FULL);
            permissions.setReadTasks(PermissionType.FULL);
            permissions.setWriteTasks(PermissionType.FULL);
            permissions.setEditTasks(PermissionType.FULL);
            permissions.setDeleteTasks(PermissionType.FULL);
            permissions.setReadStudents(PermissionType.FULL);
            permissions.setWriteStudents(PermissionType.FULL);
            permissions.setInviteStudents(PermissionType.FULL);
            permissions.setEditStudents(PermissionType.FULL);
            permissions.setDeleteStudents(PermissionType.FULL);
            permissions.setReadCourses(PermissionType.FULL);
            permissions.setWriteCourses(PermissionType.FULL);
            permissions.setEditCourses(PermissionType.FULL);
            permissions.setDeleteCourses(PermissionType.FULL);
            permissions.setReadLessons(PermissionType.FULL);
            permissions.setWriteLessons(PermissionType.FULL);
            permissions.setEditLessons(PermissionType.FULL);
            permissions.setDeleteLessons(PermissionType.FULL);
            permissions.setReadLiteratures(PermissionType.FULL);
            permissions.setWriteLiteratures(PermissionType.FULL);
            permissions.setVerifyLiteratures(PermissionType.FULL);
            permissions.setEditLiteratures(PermissionType.FULL);
            permissions.setDeleteLiteratures(PermissionType.FULL);
            permissions.setReadRoles(PermissionType.FULL);
            permissions.setWriteRoles(PermissionType.FULL);
            permissions.setEditRoles(PermissionType.FULL);
            permissions.setDeleteRoles(PermissionType.FULL);
            permissions.setReadSettings(PermissionType.FULL);
            permissions.setWriteSettings(PermissionType.FULL);
            permissions.setEditSettings(PermissionType.FULL);
            permissions.setDeleteSettings(PermissionType.FULL);

            roleRepository.save(adminRole);

            UserRole seniorManagerRole = new UserRole();
            seniorManagerRole.setName("SENIOR_MANAGER");
            permissions = seniorManagerRole.getPermissions();
            permissions.setReadStatistics(PermissionType.FULL);
            permissions.setReadTasks(PermissionType.FULL);
            permissions.setWriteTasks(PermissionType.FULL);
            permissions.setEditTasks(PermissionType.FULL);
            permissions.setDeleteTasks(PermissionType.FULL);
            permissions.setReadStudents(PermissionType.FULL);
            permissions.setWriteStudents(PermissionType.FULL);
            permissions.setInviteStudents(PermissionType.FULL);
            permissions.setEditStudents(PermissionType.FULL);
            permissions.setDeleteStudents(PermissionType.FULL);
            permissions.setReadCourses(PermissionType.FULL);
            permissions.setWriteCourses(PermissionType.FULL);
            permissions.setEditCourses(PermissionType.FULL);
            permissions.setDeleteCourses(PermissionType.FULL);
            permissions.setReadLessons(PermissionType.FULL);
            permissions.setWriteLessons(PermissionType.FULL);
            permissions.setEditLessons(PermissionType.FULL);
            permissions.setDeleteLessons(PermissionType.FULL);
            permissions.setReadLiteratures(PermissionType.FULL);
            permissions.setWriteLiteratures(PermissionType.FULL);
            permissions.setVerifyLiteratures(PermissionType.FULL);
            permissions.setEditLiteratures(PermissionType.FULL);
            permissions.setDeleteLiteratures(PermissionType.FULL);
            permissions.setReadRoles(PermissionType.FULL);
            permissions.setWriteRoles(PermissionType.NO_ACCESS);
            permissions.setEditRoles(PermissionType.NO_ACCESS);
            permissions.setDeleteRoles(PermissionType.NO_ACCESS);
            permissions.setReadSettings(PermissionType.FULL);
            permissions.setWriteSettings(PermissionType.FULL);
            permissions.setEditSettings(PermissionType.FULL);
            permissions.setDeleteSettings(PermissionType.FULL);

            roleRepository.save(seniorManagerRole);

            UserRole managerRole = new UserRole();
            managerRole.setName("MANAGER");
            permissions = managerRole.getPermissions();
            permissions.setReadStatistics(PermissionType.FULL);
            permissions.setReadTasks(PermissionType.PARTIAL);
            permissions.setWriteTasks(PermissionType.PARTIAL);
            permissions.setEditTasks(PermissionType.PARTIAL);
            permissions.setDeleteTasks(PermissionType.PARTIAL);
            permissions.setReadStudents(PermissionType.PARTIAL);
            permissions.setWriteStudents(PermissionType.PARTIAL);
            permissions.setInviteStudents(PermissionType.PARTIAL);
            permissions.setEditStudents(PermissionType.PARTIAL);
            permissions.setDeleteStudents(PermissionType.PARTIAL);
            permissions.setReadCourses(PermissionType.PARTIAL);
            permissions.setWriteCourses(PermissionType.PARTIAL);
            permissions.setEditCourses(PermissionType.PARTIAL);
            permissions.setDeleteCourses(PermissionType.PARTIAL);
            permissions.setReadLessons(PermissionType.PARTIAL);
            permissions.setWriteLessons(PermissionType.PARTIAL);
            permissions.setEditLessons(PermissionType.PARTIAL);
            permissions.setDeleteLessons(PermissionType.PARTIAL);
            permissions.setReadLiteratures(PermissionType.PARTIAL);
            permissions.setWriteLiteratures(PermissionType.PARTIAL);
            permissions.setVerifyLiteratures(PermissionType.PARTIAL);
            permissions.setEditLiteratures(PermissionType.PARTIAL);
            permissions.setDeleteLiteratures(PermissionType.PARTIAL);
            permissions.setReadRoles(PermissionType.FULL);
            permissions.setWriteRoles(PermissionType.NO_ACCESS);
            permissions.setEditRoles(PermissionType.NO_ACCESS);
            permissions.setDeleteRoles(PermissionType.NO_ACCESS);
            permissions.setReadSettings(PermissionType.FULL);
            permissions.setWriteSettings(PermissionType.NO_ACCESS);
            permissions.setEditSettings(PermissionType.NO_ACCESS);
            permissions.setDeleteSettings(PermissionType.NO_ACCESS);

            roleRepository.save(managerRole);

            UserRole mentorRole = new UserRole();
            mentorRole.setName("MENTOR");
            permissions = mentorRole.getPermissions();
            permissions.setReadStatistics(PermissionType.PARTIAL);
            permissions.setReadTasks(PermissionType.PARTIAL);
            permissions.setWriteTasks(PermissionType.PARTIAL);
            permissions.setEditTasks(PermissionType.PARTIAL);
            permissions.setDeleteTasks(PermissionType.PARTIAL);
            permissions.setReadStudents(PermissionType.PARTIAL);
            permissions.setWriteStudents(PermissionType.PARTIAL);
            permissions.setInviteStudents(PermissionType.NO_ACCESS);
            permissions.setEditStudents(PermissionType.PARTIAL);
            permissions.setDeleteStudents(PermissionType.PARTIAL);
            permissions.setReadCourses(PermissionType.PARTIAL);
            permissions.setWriteCourses(PermissionType.NO_ACCESS);
            permissions.setEditCourses(PermissionType.NO_ACCESS);
            permissions.setDeleteCourses(PermissionType.NO_ACCESS);
            permissions.setReadLessons(PermissionType.PARTIAL);
            permissions.setWriteLessons(PermissionType.PARTIAL);
            permissions.setEditLessons(PermissionType.PARTIAL);
            permissions.setDeleteLessons(PermissionType.PARTIAL);
            permissions.setReadLiteratures(PermissionType.PARTIAL);
            permissions.setWriteLiteratures(PermissionType.PARTIAL);
            permissions.setVerifyLiteratures(PermissionType.PARTIAL);
            permissions.setEditLiteratures(PermissionType.PARTIAL);
            permissions.setDeleteLiteratures(PermissionType.PARTIAL);
            permissions.setReadRoles(PermissionType.FULL);
            permissions.setWriteRoles(PermissionType.NO_ACCESS);
            permissions.setEditRoles(PermissionType.NO_ACCESS);
            permissions.setDeleteRoles(PermissionType.NO_ACCESS);
            permissions.setReadSettings(PermissionType.FULL);
            permissions.setWriteSettings(PermissionType.NO_ACCESS);
            permissions.setEditSettings(PermissionType.NO_ACCESS);
            permissions.setDeleteSettings(PermissionType.NO_ACCESS);

            roleRepository.save(mentorRole);

            UserRole studentRole = new UserRole();
            studentRole.setName("STUDENT");
            permissions = studentRole.getPermissions();
            permissions.setReadStatistics(PermissionType.NO_ACCESS);
            permissions.setReadTasks(PermissionType.NO_ACCESS);
            permissions.setWriteTasks(PermissionType.NO_ACCESS);
            permissions.setEditTasks(PermissionType.NO_ACCESS);
            permissions.setDeleteTasks(PermissionType.NO_ACCESS);
            permissions.setReadStudents(PermissionType.NO_ACCESS);
            permissions.setWriteStudents(PermissionType.NO_ACCESS);
            permissions.setInviteStudents(PermissionType.NO_ACCESS);
            permissions.setEditStudents(PermissionType.NO_ACCESS);
            permissions.setDeleteStudents(PermissionType.NO_ACCESS);
            permissions.setReadCourses(PermissionType.NO_ACCESS);
            permissions.setWriteCourses(PermissionType.NO_ACCESS);
            permissions.setEditCourses(PermissionType.NO_ACCESS);
            permissions.setDeleteCourses(PermissionType.NO_ACCESS);
            permissions.setReadLessons(PermissionType.NO_ACCESS);
            permissions.setWriteLessons(PermissionType.NO_ACCESS);
            permissions.setEditLessons(PermissionType.NO_ACCESS);
            permissions.setDeleteLessons(PermissionType.NO_ACCESS);
            permissions.setReadLiteratures(PermissionType.NO_ACCESS);
            permissions.setWriteLiteratures(PermissionType.NO_ACCESS);
            permissions.setVerifyLiteratures(PermissionType.NO_ACCESS);
            permissions.setEditLiteratures(PermissionType.NO_ACCESS);
            permissions.setDeleteLiteratures(PermissionType.NO_ACCESS);
            permissions.setReadRoles(PermissionType.NO_ACCESS);
            permissions.setWriteRoles(PermissionType.NO_ACCESS);
            permissions.setEditRoles(PermissionType.NO_ACCESS);
            permissions.setDeleteRoles(PermissionType.NO_ACCESS);
            permissions.setReadSettings(PermissionType.NO_ACCESS);
            permissions.setWriteSettings(PermissionType.NO_ACCESS);
            permissions.setEditSettings(PermissionType.NO_ACCESS);
            permissions.setDeleteSettings(PermissionType.NO_ACCESS);

            roleRepository.save(studentRole);


        }
    }
    private void checkForAdmins() {
        if(adminRepository.count() < 1) {
            log.info("Adding default admins");

            Admin director = new Admin();
            director.setRole(roleRepository.getReferenceByName("ADMIN"));
            director.setFirstName("DEFAULT");
            director.setLastName("ADMIN");
            director.setPhone("+380500000000");
            director.setEmail("admin@gmail.com");
            director.setPassword(passwordEncoder.encode("admin"));
            director.setAvatar("placeholder.jpg");

            adminRepository.save(director);

            Admin sManager = new Admin();
            sManager.setRole(roleRepository.getReferenceByName("SENIOR_MANAGER"));
            sManager.setFirstName("DEFAULT");
            sManager.setLastName("S.MANAGER");
            sManager.setPhone("+380500000000");
            sManager.setEmail("seniormanager@gmail.com");
            sManager.setPassword(passwordEncoder.encode("seniormanager"));
            sManager.setAvatar("placeholder.jpg");

            adminRepository.save(sManager);

            Admin manager = new Admin();
            manager.setRole(roleRepository.getReferenceByName("MANAGER"));
            manager.setFirstName("DEFAULT");
            manager.setLastName("MANAGER");
            manager.setPhone("+380500000000");
            manager.setEmail("manager@gmail.com");
            manager.setPassword(passwordEncoder.encode("manager"));
            manager.setAvatar("placeholder.jpg");

            adminRepository.save(manager);

            Admin mentor = new Admin();
            mentor.setRole(roleRepository.getReferenceByName("MENTOR"));
            mentor.setFirstName("DEFAULT");
            mentor.setLastName("MENTOR");
            mentor.setPhone("+380500000000");
            mentor.setEmail("mentor@gmail.com");
            mentor.setPassword(passwordEncoder.encode("mentor"));
            mentor.setAvatar("placeholder.jpg");

            adminRepository.save(mentor);

        }
    }
    private void checkForCourses() {
        if(courseRepository.count() < 1) {

            log.info("Adding default courses");

            Course javaCourse = new Course();
            javaCourse.setName("Java");
            javaCourse.setMentor(adminRepository.findById(4L).orElse(null));
            javaCourse.setManager(adminRepository.findById(3L).orElse(null));
            javaCourse.setStatus(CourseStatus.ACTIVE);
            javaCourse.setIcon("java.png");
            CourseInfo info = new CourseInfo();
            info.setMain_description("Main Description For Java Course");
            info.setTopics(List.of("Topic 1", "Topic 2", "Topic 3"));
            info.setCompletionTime("6-9");
            info.setCompletionTimeUnit(TimeUnit.MONTHS);
            info.setGroupSize(15);
            info.setHoursNorm(35);
            info.setLessonInterval(7);
            info.setBeginDate(LocalDate.now());
            javaCourse.setCourseInfo(info);
            courseRepository.save(javaCourse);

            Course jsCourse = new Course();
            jsCourse.setName("JS");
            jsCourse.setMentor(adminRepository.findById(4L).orElse(null));
            jsCourse.setManager(adminRepository.findById(3L).orElse(null));
            jsCourse.setStatus(CourseStatus.ACTIVE);
            jsCourse.setIcon("js.png");
            info = new CourseInfo();
            info.setMain_description("Main Description For JS Course");
            info.setTopics(List.of("Topic 1", "Topic 2", "Topic 3"));
            info.setCompletionTime("6-9");
            info.setCompletionTimeUnit(TimeUnit.MONTHS);
            info.setGroupSize(15);
            info.setHoursNorm(35);
            info.setLessonInterval(7);
            info.setBeginDate(LocalDate.now());
            jsCourse.setCourseInfo(info);
            courseRepository.save(jsCourse);

            Course pythonCourse = new Course();
            pythonCourse.setName("Python");
            pythonCourse.setMentor(adminRepository.findById(4L).orElse(null));
            pythonCourse.setManager(adminRepository.findById(3L).orElse(null));
            pythonCourse.setStatus(CourseStatus.ACTIVE);
            pythonCourse.setIcon("python.png");
            info = new CourseInfo();
            info.setMain_description("Main Description For Python Course");
            info.setTopics(List.of("Topic 1", "Topic 2", "Topic 3"));
            info.setCompletionTime("6-9");
            info.setCompletionTimeUnit(TimeUnit.MONTHS);
            info.setGroupSize(15);
            info.setHoursNorm(35);
            info.setLessonInterval(7);
            info.setBeginDate(LocalDate.now());
            pythonCourse.setCourseInfo(info);

            courseRepository.save(pythonCourse);
        }
    }
    private void checkForContacts() {
        if(contactRepository.count() < 1) {

            log.info("Adding default contacts");

            adminRepository.findAll().forEach(admin -> {
                ContactInfo contact = new ContactInfo();
                contact.setAdmin(admin);
                contact.setEmail(admin.getEmail());
                contact.setPhone(admin.getPhone());
                contact.setTelegram("@telegram");

                contactRepository.save(contact);
            });
        }
    }
    private void checkForStudents() {
        if(studentRepository.count() < 1) {

            log.info("Adding default students");

            Student student1 = new Student();
            student1.setAvatar("1.png");
            student1.setRole(roleRepository.getReferenceByName("STUDENT"));
            student1.setPassword(passwordEncoder.encode("student1"));
            student1.setRating(10);
            student1.setCourse(courseRepository.findById(1L).orElse(null));

            StudentDetails details = student1.getDetails();
            details.setFirstName("Student");
            details.setLastName("Default");
            details.setFathersName("Default");
            details.setPhone("+380500000000");
            details.setEmail("student1@gmail.com");
            details.setTelegram("@student1");
            details.setAddress("default address");
            details.setGithubLink("default github link");
            details.setLinkedinLink("default linkedin link");
            details.setBirthdate(LocalDate.of(2000,1,1));
            details.setEducationLevel(StudentEducationLevel.BACHELOR);
            details.setEnglishLevel(StudentEnglishLevel.B1);
            details.setWorkStatus(StudentWorkStatus.UNEMPLOYED);
            details.setAccountStatus(StudentAccountStatus.ACTIVE);

            courseRepository.findByName("Java").ifPresentOrElse(c -> {
                student1.setCourse(c);
                c.getStudents().add(studentRepository.save(student1));
            }, () -> studentRepository.save(student1));

            Student student2 = new Student();
            student2.setAvatar("2.png");
            student2.setRole(roleRepository.getReferenceByName("STUDENT"));
            student2.setPassword(passwordEncoder.encode("student2"));
            student2.setRating(10);
            student2.setCourse(courseRepository.findById(2L).orElse(null));
            details = student2.getDetails();
            details.setFirstName("Student");
            details.setLastName("Default");
            details.setFathersName("Default");
            details.setPhone("+380500000000");
            details.setEmail("student2@gmail.com");
            details.setTelegram("@student2");
            details.setAddress("default address");
            details.setGithubLink("default github link");
            details.setLinkedinLink("default linkedin link");
            details.setBirthdate(LocalDate.of(2000,1,1));
            details.setEducationLevel(StudentEducationLevel.BACHELOR);
            details.setEnglishLevel(StudentEnglishLevel.B1);
            details.setWorkStatus(StudentWorkStatus.UNEMPLOYED);
            details.setAccountStatus(StudentAccountStatus.ACTIVE);

            courseRepository.findByName("JS").ifPresentOrElse(c -> {
                student2.setCourse(c);
                c.getStudents().add(studentRepository.save(student2));
            }, () -> studentRepository.save(student2));

            Student student3 = new Student();
            student3.setAvatar("3.png");
            student3.setRole(roleRepository.getReferenceByName("STUDENT"));
            student3.setPassword(passwordEncoder.encode("student3"));
            student3.setRating(10);
            student3.setCourse(courseRepository.findById(3L).orElse(null));
            details = student3.getDetails();
            details.setFirstName("Student");
            details.setLastName("Default");
            details.setFathersName("Default");
            details.setPhone("+380500000000");
            details.setEmail("student3@gmail.com");
            details.setTelegram("@student3");
            details.setAddress("default address");
            details.setGithubLink("default github link");
            details.setLinkedinLink("default linkedin link");
            details.setBirthdate(LocalDate.of(2000,1,1));
            details.setEducationLevel(StudentEducationLevel.BACHELOR);
            details.setEnglishLevel(StudentEnglishLevel.B1);
            details.setWorkStatus(StudentWorkStatus.UNEMPLOYED);
            details.setAccountStatus(StudentAccountStatus.ACTIVE);

            courseRepository.findByName("Python").ifPresentOrElse(c -> {
                student3.setCourse(c);
                c.getStudents().add(studentRepository.save(student3));
            }, () -> studentRepository.save(student3));
        }
    }
    private void checkForTasks() {
        if(taskRepository.count() < 1) {
            log.info("Adding default tasks");

            Task task = new Task();
            task.setCourse(courseRepository.findById(1L).orElseThrow());
            task.setName("TestTask1");
            task.setLevel(TaskLevel.ADVANCED);
            task.setSkillsDescription("SkillsDescription");
            task.setTaskDescription("TaskDescription");
            task.setCompletionTime(new CompletionTime().setValue("6-8").setTimeUnit(TimeUnit.MONTHS));
            task.setStatus(TaskStatus.ACTIVE);

            task = taskRepository.save(task);

            Task task2 = new Task();
            task2.setParentTask(task);
            task2.setCourse(courseRepository.findById(1L).orElseThrow());
            task2.setName("TestTask1 Subtask");
            task2.setLevel(TaskLevel.ADVANCED);
            task2.setSkillsDescription("SkillsDescription");
            task2.setTaskDescription("TaskDescription");
            task2.setCompletionTime(new CompletionTime().setValue("6-8").setTimeUnit(TimeUnit.MONTHS));
            task2.setStatus(TaskStatus.ACTIVE);

            task2 = taskRepository.save(task2);
            task.getSubtasks().add(task2);
            taskRepository.save(task);

            Task task3 = new Task();
            task3.setCourse(courseRepository.findById(2L).orElseThrow());
            task3.setName("TestTask2");
            task3.setLevel(TaskLevel.ADVANCED);
            task3.setSkillsDescription("SkillsDescription");
            task3.setTaskDescription("TaskDescription");
            task3.setCompletionTime(new CompletionTime().setValue("6-8").setTimeUnit(TimeUnit.MONTHS));
            task3.setStatus(TaskStatus.ACTIVE);

            taskRepository.save(task3);
        }
    }
    private void checkForLiterature() {
        if(literatureRepository.count() < 1) {
            log.info("Adding default literature");

            Literature lit = new Literature();
            lit.setName("Test");
            lit.setAuthor("Test Author");
            lit.setCourse(courseRepository.findById(1L).orElseThrow());
            lit.setType(LiteratureType.LINK);
            lit.setKeywords("Test1, Test2, Test3");
            lit.setDescription("Test Description");
            lit.setResource_link("http://www.lol.com");

            literatureRepository.save(lit);

            Literature lit2 = new Literature();
            lit2.setName("Test2");
            lit2.setAuthor("Test2 Author");
            lit2.setCourse(courseRepository.findById(2L).orElseThrow());
            lit2.setType(LiteratureType.BOOK);
            lit2.setKeywords("Test1, Test2, Test3");
            lit2.setDescription("Test2 Description");
            lit2.setResource_link("test.pdf");

            literatureRepository.save(lit2);
        }
    }
    private void checkForLessons() {
        if(lessonRepository.count() < 1) {
            log.info("Adding default lessons");

            Lesson lesson = new Lesson();
            lesson.setDatetime(LocalDateTime.now().plusDays(1));
            lesson.setCourse(courseRepository.findById(1L).orElseThrow());
            lesson.setLink("http://www.test.com");
            lesson.setStatus(LessonStatus.PLANNED);
            LessonReport report = new LessonReport();
            report.setLesson(lesson);

            lessonRepository.save(lesson);

            Lesson lesson2 = new Lesson();
            lesson2.setDatetime(LocalDateTime.now().plusDays(1));
            lesson2.setCourse(courseRepository.findById(2L).orElseThrow());
            lesson2.setLink("http://www.test.com");
            lesson2.setStatus(LessonStatus.PLANNED);
            LessonReport report2 = new LessonReport();
            report2.setLesson(lesson2);

            lessonRepository.save(lesson2);

            Lesson lesson3 = new Lesson();
            lesson3.setDatetime(LocalDateTime.now().plusDays(1));
            lesson3.setCourse(courseRepository.findById(3L).orElseThrow());
            lesson3.setLink("http://www.test.com");
            lesson3.setStatus(LessonStatus.PLANNED);
            LessonReport report3 = new LessonReport();
            report3.setLesson(lesson3);

            lessonRepository.save(lesson3);
        }
    }

}
