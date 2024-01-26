package com.example.spacelab.validator;

import com.example.spacelab.dto.course.*;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.repository.AdminRepository;
import com.example.spacelab.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.example.spacelab.util.ValidationUtils.*;

@Component
@RequiredArgsConstructor
public class CourseValidator implements Validator {


    private final CourseRepository courseRepository;
    private final AdminRepository adminRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Course.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors e) {
        CourseEditDTO dto = (CourseEditDTO) target;

        if(fieldIsEmpty(dto.getName()))
            e.rejectValue("name", "name.empty", "validation.field.empty");
        else if(fieldMaxLengthIsIncorrect(dto.getName(), 100))
            e.rejectValue("name", "name.length", "validation.field.length.max");
        else if(courseExists(dto.getName()) && !courseHasSameId(dto.getId(), courseRepository.findByName(dto.getName()).orElseThrow())) {
            e.rejectValue("name", "name.exists", "validation.course.name.exists");
        }

        CourseInfoDTO info = dto.getInfo();

        if(fieldIsEmpty(info.getDescription()))
            e.rejectValue("info.description", "info.description.empty", "validation.field.empty");
        else if(info.getDescription().length() > 3000)
            e.rejectValue("info.description", "info.description.length", "validation.field.length.max");

        if(info.getTopics() == null || info.getTopics().size() == 0)
            e.rejectValue("info.topics", "info.topics.empty", "validation.course.topics.empty");
        else for(int i = 0; i < info.getTopics().size(); i++) {
            if(info.getTopics().get(i).length() > 250)
                e.rejectValue("info.topics["+i+"]", "info.topics["+i+"].length", "validation.field.length.max");
        }

        CourseSettingsDTO settings = info.getSettings();

        if(settings.groupSize() == null)
            e.rejectValue("info.settings.groupSize", "info.settings.groupSize.empty", "validation.field.empty");
        else if(settings.groupSize() < 3 || settings.groupSize() > 30)
            e.rejectValue("info.settings.groupSize", "info.settings.groupSize.length", "validation.course.group-size");

        if(settings.hoursNorm() == null)
            e.rejectValue("info.settings.hoursNorm", "info.settings.hoursNorm.empty", "validation.field.empty");
        else if(settings.hoursNorm() < 10 || settings.hoursNorm() > 40)
            e.rejectValue("info.settings.hoursNorm", "info.settings.hoursNorm.length", "validation.course.hours-norm");

        if(settings.lessonInterval() == null)
            e.rejectValue("info.settings.lessonInterval", "info.settings.lessonInterval.empty", "validation.field.empty");
        else if(settings.lessonInterval() < 1 || settings.lessonInterval() > 30)
            e.rejectValue("info.settings.lessonInterval", "info.settings.lessonInterval.length", "validation.course.lesson-interval");

        CourseMembersDTO members = dto.getMembers();

//        if(members.getStudents() == null || members.getStudents().size() < 1)
//            e.rejectValue("members.students", "members.students.empty", "Add students!");
//        else if(members.getStudents().size() > 30)
//            e.rejectValue("members.students", "members.students.size", "Course can't have more than 30 students!");

        //structure validation

    }

    private boolean courseExists(String courseName) {
        return courseRepository.existsByName(courseName);
    }

    private boolean courseHasSameId(Long id, Course course) {
        return course.getId().equals(id);
    }
}
