package com.example.spacelab.validator;

import com.example.spacelab.dto.course.CourseSaveUpdatedDTO;
import com.example.spacelab.dto.task.TaskSaveDTO;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.repository.AdminRepository;
import com.example.spacelab.repository.CourseRepository;
import com.example.spacelab.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.example.spacelab.util.ValidationUtils.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Task.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors e) {
        TaskSaveDTO dto = (TaskSaveDTO) target;
        log.info("dto to validate: {}", dto);

        if (fieldIsEmpty(dto.getName()))
            e.rejectValue("name", "name.empty", "validation.task.name.empty");
        else if (fieldLengthIsIncorrect(dto.getName(), 3, 100))
            e.rejectValue("name", "name.length", "validation.task.name.length");

//        if (dto.getCourseID() == null || dto.getCourseID() == 0)
//            e.rejectValue("courseID", "courseID.empty", "Select course!");
//        else if (dto.getCourseID() != -1 && !courseRepository.existsById(dto.getCourseID()))
//            e.rejectValue("courseID", "courseID.no-match", "Course with this ID doesn't exist!");

        if (fieldIsEmpty(dto.getLevel()))
            e.rejectValue("level", "level.empty", "validation.task.level.empty");

        if (fieldIsEmpty(dto.getStatus()))
            e.rejectValue("status", "status.empty", "validation.task.status.empty");

        if (fieldIsEmpty(dto.getCompletionTime())) {
//            e.rejectValue("completionTime", "completionTime.empty", "Enter completion time!");
        }
        else if (fieldMaxLengthIsIncorrect(dto.getCompletionTime(), 7)) {
            e.rejectValue("completionTime", "completionTime.length", "validation.task.completionTime.length");
        }

        if(fieldIsNotEmpty(dto.getCompletionTime()) && fieldIsEmpty(dto.getCompletionTimeUnit())) {
            e.rejectValue("completionTimeUnit", "completionTime.length", "validation.task.completionTimeUnit.empty");
        }


        if (fieldIsEmpty(dto.getSkillsDescription())) {
//            e.rejectValue("skillsDescription", "skillsDescription.empty", "Enter skills description!");
        }
        else if (fieldLengthIsIncorrect(dto.getSkillsDescription(), 3, 200)) {
            e.rejectValue("skillsDescription", "skillsDescription.length", "validation.task.skillsDescription.length");
        }

        if (fieldIsEmpty(dto.getTaskDescription()))
            e.rejectValue("taskDescription", "taskDescription.empty", "validation.task.taskDescription.empty");
        else if (fieldLengthIsIncorrect(dto.getTaskDescription(), 3, 4000))
            e.rejectValue("taskDescription", "taskDescription.length", "validation.task.taskDescription.length");

    }
}
