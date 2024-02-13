package com.example.spacelab.validator;

import com.example.spacelab.dto.course.*;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.repository.AdminRepository;
import com.example.spacelab.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

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
        else if(fieldMaxLengthIsIncorrect(dto.getInfo().getDescription(), 3000))
            e.rejectValue("info.description", "info.description.length", "validation.field.length.max");

        if(info.getTopics() == null || info.getTopics().size() == 0)
            e.rejectValue("info.topics", "info.topics.empty", "validation.course.topics.empty");
        else for(int i = 0; i < info.getTopics().size(); i++) {
            String topic = info.getTopics().get(i);
            if(fieldIsEmpty(topic)) {
                e.rejectValue("info.topics["+i+"]", "info.topics["+i+"].empty", "validation.field.empty");
            }
            else if(fieldMaxLengthIsIncorrect(info.getTopics().get(i), 250)) {
                e.rejectValue("info.topics["+i+"]", "info.topics["+i+"].length", "validation.field.length.max");
            }
        }

        CourseSettingsDTO settings = info.getSettings();

        if(fieldIsEmpty(settings.completionTime())) {
            e.rejectValue("info.settings.completionTime", "info.settings.completionTime.empty", "validation.field.empty");
        }
        else if(fieldMaxLengthIsIncorrect(settings.completionTime(), 7)) {
            e.rejectValue("info.settings.completionTime", "info.settings.completionTime.empty", "validation.field.length.max");
        }

        if(settings.completionTimeUnit() == null) {
            e.rejectValue("info.settings.completionTimeUnit", "info.settings.completionTimeUnit.empty", "validation.field.empty");
        }

        if(settings.groupSize() == null)
            e.rejectValue("info.settings.groupSize", "info.settings.groupSize.empty", "validation.field.empty");
        else if(fieldIntValueIsIncorrect(settings.groupSize(), MIN_GROUP_SIZE, MAX_GROUP_SIZE))
            e.rejectValue("info.settings.groupSize", "info.settings.groupSize.length", "validation.course.group-size");

        if(settings.hoursNorm() == null)
            e.rejectValue("info.settings.hoursNorm", "info.settings.hoursNorm.empty", "validation.field.empty");
        else if(fieldIntValueIsIncorrect(settings.hoursNorm(), MIN_HOURS_NORM, MAX_HOURS_NORM))
            e.rejectValue("info.settings.hoursNorm", "info.settings.hoursNorm.length", "validation.course.hours-norm");

        if(settings.lessonInterval() == null)
            e.rejectValue("info.settings.lessonInterval", "info.settings.lessonInterval.empty", "validation.field.empty");
        else if(fieldIntValueIsIncorrect(settings.lessonInterval(), MIN_LESSON_INTERVAL, MAX_LESSON_INTERVAL))
            e.rejectValue("info.settings.lessonInterval", "info.settings.lessonInterval.length", "validation.course.lesson-interval");

        CourseMembersDTO members = dto.getMembers();

//        if(members.getStudents() == null || members.getStudents().size() < 1)
//            e.rejectValue("members.students", "members.students.empty", "Add students!");
//        else if(members.getStudents().size() > 30)
//            e.rejectValue("members.students", "members.students.size", "Course can't have more than 30 students!");

        //structure validation

    }

    public void validateIcon(CourseIconDTO dto, Errors e) {
        MultipartFile icon = dto.icon();
        if(icon.isEmpty()) {
            e.rejectValue("icon", "icon.empty", "validation.file.upload");
        }
        else if(icon.getSize() > MAX_IMAGE_SIZE) {
            e.rejectValue("icon", "icon.max-size", "validation.file.max-size");
        }
        else {
            String filename = icon.getOriginalFilename();
            String extension = filename.substring(filename.lastIndexOf(".")+1);
            if(!ALLOWED_IMAGE_FORMATS.contains(extension)) {
                e.rejectValue("icon", "icon.format", "validation.file.formats.allowed");
            }
        }
    }

    private boolean courseExists(String courseName) {
        return courseRepository.existsByName(courseName);
    }

    private boolean courseHasSameId(Long id, Course course) {
        return course.getId().equals(id);
    }
}
