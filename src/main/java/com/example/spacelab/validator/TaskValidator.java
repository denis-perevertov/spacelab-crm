package com.example.spacelab.validator;

import com.example.spacelab.dto.task.TaskFileDTO;
import com.example.spacelab.dto.task.TaskProgressPointDTO;
import com.example.spacelab.dto.task.TaskSaveDTO;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.model.task.TaskFile;
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
            e.rejectValue("name", "name.empty", "validation.field.empty");
        else if (fieldMaxLengthIsIncorrect(dto.getName(),  100))
            e.rejectValue("name", "name.length", "validation.field.length.max");

//        if (dto.getCourseID() == null || dto.getCourseID() == 0)
//            e.rejectValue("courseID", "courseID.empty", "Select course!");
//        else if (dto.getCourseID() != -1 && !courseRepository.existsById(dto.getCourseID()))
//            e.rejectValue("courseID", "courseID.no-match", "Course with this ID doesn't exist!");

        if (fieldIsEmpty(dto.getLevel()))
            e.rejectValue("level", "level.empty", "validation.field.select");

        if (fieldIsEmpty(dto.getStatus()))
            e.rejectValue("status", "status.empty", "validation.field.select");

        if (fieldIsEmpty(dto.getCompletionTime())) {
            e.rejectValue("completionTime", "completionTime.empty", "validation.field.empty");
        }
        else if (fieldMaxLengthIsIncorrect(dto.getCompletionTime(), 7)) {
            e.rejectValue("completionTime", "completionTime.length", "validation.field.length.max");
        }

        if(fieldIsNotEmpty(dto.getCompletionTime()) && fieldIsEmpty(dto.getCompletionTimeUnit())) {
            e.rejectValue("completionTime", "completionTime.length", "validation.task.completion-time-unit.empty");
        }

        if (fieldIsEmpty(dto.getSkillsDescription())) {
//            e.rejectValue("skillsDescription", "skillsDescription.empty", "Enter skills description!");
        }
        else if (fieldMaxLengthIsIncorrect(dto.getSkillsDescription(), 200)) {
            e.rejectValue("skillsDescription", "skillsDescription.length", "validation.field.length.max");
        }

        if (fieldIsEmpty(dto.getTaskDescription()))
            e.rejectValue("taskDescription", "taskDescription.empty", "validation.field.empty");
        else if (fieldMaxLengthIsIncorrect(dto.getTaskDescription(), 4000))
            e.rejectValue("taskDescription", "taskDescription.length", "validation.field.length.max");

        // todo validate points
        if(dto.getTaskProgressPoints() != null && !dto.getTaskProgressPoints().isEmpty()) {
            for(int i = 0; i < dto.getTaskProgressPoints().size(); i++) {
                TaskProgressPointDTO point = dto.getTaskProgressPoints().get(i);
                if(point.name().length() > 255) {
                    e.rejectValue("taskProgressPoints["+i+"]", "taskProgressPoints["+i+"].length", "validation.field.length.max");
                }
                if(!point.subpoints().isEmpty()) {
                    for(int j = 0; j < point.subpoints().size(); j++) {
                        if(point.subpoints().get(j).name().length() > 255) {
                            e.rejectValue("taskProgressPoints["+i+"].subpoints["+j+"]", "length", "validation.field.length.max");
                        }
                    }
                }

            }
        }

        // todo validate task files
        if(dto.getFiles() != null && !dto.getFiles().isEmpty()) {
            for (int i = 0; i < dto.getFiles().size(); i++) {
                TaskFileDTO file = dto.getFiles().get(i);
                if(fieldIsEmpty(file.name())) {
                    e.rejectValue("taskFiles["+i+"]", "taskFiles["+i+"].empty", "validation.field.empty");
                }
                else if(fieldMaxLengthIsIncorrect(file.name(), 255)) {
                    e.rejectValue("taskFiles["+i+"]", "taskFiles["+i+"].length", "validation.field.length.max");
                }
                else if(file.file() != null && file.file().getSize() > MAX_FILE_SIZE) {
                    e.rejectValue("taskFiles["+i+"]", "taskFiles["+i+"].max-size", "validation.file.max-size");
                }
            }
        }

    }
}
