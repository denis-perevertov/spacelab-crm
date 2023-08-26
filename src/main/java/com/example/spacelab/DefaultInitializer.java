package com.example.spacelab;

import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.contact.ContactInfo;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.course.CourseInfo;
import com.example.spacelab.model.role.PermissionSet;
import com.example.spacelab.model.role.PermissionType;
import com.example.spacelab.model.role.UserRole;
import com.example.spacelab.model.student.*;
import com.example.spacelab.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Log
@RequiredArgsConstructor
public class DefaultInitializer implements CommandLineRunner {

    private final UserRoleRepository roleRepository;
    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final ContactInfoRepository contactRepository;
    private final CourseRepository courseRepository;

    private final PasswordEncoder passwordEncoder;

    /*

    in progress

    private final TaskRepository taskRepository;
    private final LiteratureRepository literatureRepository;
    private final LessonRepository lessonRepository;
     */

    @Override
    public void run(String... args) throws InterruptedException {



        Thread.sleep(50); checkForRoles();
        Thread.sleep(50); checkForAdmins();
        Thread.sleep(50); checkForCourses();
        Thread.sleep(50); checkForContacts();
        Thread.sleep(50); checkForStudents();
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
            javaCourse.setBeginningDate(LocalDate.of(2023,8,1));
            javaCourse.setMentor(adminRepository.findById(4L).orElse(null));
            javaCourse.setManager(adminRepository.findById(5L).orElse(null));
            javaCourse.setIsActive(true);
            CourseInfo info = javaCourse.getCourseInfo();
            info.setMain_description("Main Description For Java Course");
            info.setTopics(List.of("Topic 1", "Topic 2", "Topic 3"));
            info.setCompletionTime("6-9 months");
            info.setGroupSize(15);
            info.setHoursNorm(35);

            courseRepository.save(javaCourse);

            Course jsCourse = new Course();
            jsCourse.setName("JS");
            jsCourse.setBeginningDate(LocalDate.of(2023,8,1));
            jsCourse.setMentor(adminRepository.findById(4L).orElse(null));
            jsCourse.setManager(adminRepository.findById(5L).orElse(null));
            jsCourse.setIsActive(true);
            info = jsCourse.getCourseInfo();
            info.setMain_description("Main Description For JS Course");
            info.setTopics(List.of("Topic 1", "Topic 2", "Topic 3"));
            info.setCompletionTime("6-9 months");
            info.setGroupSize(15);
            info.setHoursNorm(35);

            courseRepository.save(jsCourse);

            Course pythonCourse = new Course();
            pythonCourse.setName("Python");
            pythonCourse.setBeginningDate(LocalDate.of(2023,8,1));
            pythonCourse.setMentor(adminRepository.findById(4L).orElse(null));
            pythonCourse.setManager(adminRepository.findById(5L).orElse(null));
            pythonCourse.setIsActive(true);
            info = pythonCourse.getCourseInfo();
            info.setMain_description("Main Description For Python Course");
            info.setTopics(List.of("Topic 1", "Topic 2", "Topic 3"));
            info.setCompletionTime("6-9 months");
            info.setGroupSize(15);
            info.setHoursNorm(35);

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
                contact.setTelegram("@contact");

                contactRepository.save(contact);
            });
        }
    }

    private void checkForStudents() {
        if(studentRepository.count() < 1) {

            log.info("Adding default students");

            Student student1 = new Student();
            student1.setAvatar("placeholder.jpg");
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

            studentRepository.save(student1);

            Student student2 = new Student();
            student2.setAvatar("placeholder.jpg");
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

            studentRepository.save(student2);

            Student student3 = new Student();
            student3.setAvatar("placeholder.jpg");
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

            studentRepository.save(student3);
        }
    }

}
