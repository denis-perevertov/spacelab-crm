package com.example.spacelab.validator;

import com.example.spacelab.dto.lesson.LessonReportRowSaveDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.example.spacelab.util.ValidationUtils.*;

@Component
@RequiredArgsConstructor
public class LessonReportRowValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return LessonReportRowSaveDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors e) {
        LessonReportRowSaveDTO dto = (LessonReportRowSaveDTO) target;

        if(dto.getHours() != null && (dto.getHours() < 0.1 || dto.getHours() > 999.0)) {
            e.rejectValue("hours", "hours.empty", "Write correct hours");
        }

        if(dto.isHoursNeedComment()) {
            if(dto.getNote() == null || dto.getNote().isEmpty()) {
                e.rejectValue("note", "note.empty", "Enter note");
            }
            else if(dto.getNote().length() > 255) {
                e.rejectValue("note", "note.length", "Max length - 255");
            }
        }

        if(dto.getComment() != null || (dto.getComment().length() > 1000)) {
            e.rejectValue("comment", "comment.length", "Max length - 1000");
        }

        if(dto.getRating() == null) {
            e.rejectValue("rating", "rating.empty", "Write rating");
        }
        else if(dto.getRating() < 0 || dto.getRating() > 100) {
            e.rejectValue("rating", "rating.length", "Rating - 0-100");
        }
    }
}
