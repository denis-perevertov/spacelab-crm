package com.example.spacelab.validator;

import com.example.spacelab.dto.course.*;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.repository.AdminRepository;
import com.example.spacelab.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

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

        if(dto.getName() == null || dto.getName().isEmpty())
            e.rejectValue("name", "name.empty", "Enter name!");
        else if(dto.getName().length() < 2 || dto.getName().length() > 100)
            e.rejectValue("name", "name.length", "Name length: 2-100");

        CourseInfoDTO info = dto.getInfo();

        if(info.getDescription() == null || info.getDescription().isEmpty())
            e.rejectValue("info.description", "info.description.empty", "Enter description!");
        else if(info.getDescription().length() > 3000 || info.getDescription().length() < 20)
            e.rejectValue("info.description", "info.description.length", "Description length: 20-3000");

        if(info.getTopics() == null || info.getTopics().size() == 0)
            e.rejectValue("info.topics", "info.topics.empty", "Enter course topics!");
        else for(int i = 0; i < info.getTopics().size(); i++) {
            if(info.getTopics().get(i).length() < 2 || info.getTopics().get(i).length() > 250)
                e.rejectValue("info.topics["+i+"]", "info.topics["+i+"].length", "Field length: 2-250");
        }

        CourseSettingsDTO settings = info.getSettings();
        if(settings.getProgramDuration().getValue() == null || settings.getProgramDuration().getValue().isEmpty())
            e.rejectValue("info.settings.programDuration", "info.settings.programDuration.empty", "Enter program duration!");
        else if(settings.getProgramDuration().getValue().length() < 2 || settings.getProgramDuration().getValue().length() > 10)
                e.rejectValue("info.settings.programDuration", "info.settings.programDuration.length", "Field length: 2-10");

        if(settings.getGroupSize() == null)
            e.rejectValue("info.settings.groupSize", "info.settings.groupSize.empty", "Enter group size!");
        else if(settings.getGroupSize() < 3 || settings.getGroupSize() > 30)
            e.rejectValue("info.settings.groupSize", "info.settings.groupSize.length", "Group size: 3-30!");

        if(settings.getHoursNorm() == null)
            e.rejectValue("info.settings.hoursNorm", "info.settings.hoursNorm.empty", "Enter hours norm!");
        else if(settings.getHoursNorm() < 10 || settings.getHoursNorm() > 40)
            e.rejectValue("info.settings.hoursNorm", "info.settings.hoursNorm.length", "Hours norm: 10-40!");

        CourseMembersDTO members = dto.getMembers();

        if(members.getStudents() == null || members.getStudents().size() < 1)
            e.rejectValue("members.students", "members.students.empty", "Add students!");
        else if(members.getStudents().size() > 30)
            e.rejectValue("members.students", "members.students.size", "Course can't have more than 30 students!");

        //structure validation

    }
}
