package com.example.spacelab.validator;

import com.example.spacelab.dto.course.CourseSaveUpdatedDTO;
import com.example.spacelab.dto.task.TaskSaveDTO;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.repository.AdminRepository;
import com.example.spacelab.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class TaskValidator implements Validator {

    private final CourseRepository courseRepository;
    private final AdminRepository adminRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Task.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors e) {
        TaskSaveDTO dto = (TaskSaveDTO) target;


        if (dto.getName() == null || dto.getName().isEmpty())
            e.rejectValue("name", "name.empty", "Enter name!");
        else if (dto.getName().length() < 3 || dto.getName().length() > 100)
            e.rejectValue("name", "name.length", "Name length: 3-100");


        if (dto.getCourseID() == null || dto.getCourseID() == 0)
            e.rejectValue("courseID", "courseID.empty", "Select course!");
        else if (!courseRepository.existsById(dto.getCourseID()))
            e.rejectValue("courseID", "courseID.no-match", "Course with this ID doesn't exist!");


        if (dto.getLevel() == null)
            e.rejectValue("level", "level.empty", "Select level of task!");


        if (dto.getStatus() == null)
            e.rejectValue("status", "status.empty", "Select status of task!");


        if (dto.getCompletionTime() == null || dto.getCompletionTime().isEmpty())
            e.rejectValue("completionTime", "completionTime.empty", "Enter completion time!");
        else if (dto.getCompletionTime().length() < 2 || dto.getCompletionTime().length() > 100)
            e.rejectValue("completionTime", "completionTime.length", "Completion time length: 2-100");


        if (dto.getSkillsDescription() == null || dto.getSkillsDescription().isEmpty())
            e.rejectValue("skillsDescription", "skillsDescription.empty", "Enter skills description!");
        else if (dto.getSkillsDescription().length() < 3 || dto.getSkillsDescription().length() > 400)
            e.rejectValue("skillsDescription", "skillsDescription.length", "Skills description length: 3-400");


        if (dto.getTaskDescription() == null || dto.getTaskDescription().isEmpty())
            e.rejectValue("taskDescription", "taskDescription.empty", "Enter task description!");
        else if (dto.getTaskDescription().length() < 3 || dto.getTaskDescription().length() > 3000)
            e.rejectValue("taskDescription", "taskDescription.length", "Task description length: 2-100");

    }
}
