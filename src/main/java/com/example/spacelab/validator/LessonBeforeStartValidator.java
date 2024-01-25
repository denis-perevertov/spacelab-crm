package com.example.spacelab.validator;

import com.example.spacelab.dto.course.CourseSaveUpdatedDTO;
import com.example.spacelab.dto.lesson.LessonSaveBeforeStartDTO;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.lesson.Lesson;
import com.example.spacelab.model.lesson.LessonStatus;
import com.example.spacelab.repository.AdminRepository;
import com.example.spacelab.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class LessonBeforeStartValidator implements Validator {

    private final AdminRepository adminRepository;
    private final CourseRepository courseRepository;
    @Override
    public boolean supports(Class<?> clazz) {
        return Lesson.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors e) {
        LessonSaveBeforeStartDTO dto = (LessonSaveBeforeStartDTO) target;

        // todo add datetime check

//        LocalDateTime datetime = dto.getLessonStartTime();
//        if(datetime == null) {
//            e.rejectValue("lessonStartTime", "empty", "Enter datetime!");
//        }
//        else if(datetime.isBefore(LocalDateTime.now())) {
//            e.rejectValue("lessonStartTime", "past", "datetime is in the past");
//        }

        if(dto.getCourseID() == null || dto.getCourseID() < 0)
            e.rejectValue("courseID", "courseID.empty", "Select course!");
        else if(!courseRepository.existsById(dto.getCourseID()))
            e.rejectValue("courseID", "courseID.no-match", "Course with this ID doesn't exist!");

        if(dto.getLink() == null || dto.getLink().isEmpty())
            e.rejectValue("link", "link.empty", "Enter link!");
        else if(dto.getLink().length() > 200)
            e.rejectValue("link", "link.length", "Link length: max 200");

//        if(dto.getStatus().equals(LessonStatus.ACTIVE) && (dto.getId() != null && dto.getId() != 0)) {
//            e.rejectValue("status", "status.active", "Cannot create/edit an active lesson!");
//        }

        if(dto.getStatus().equals(LessonStatus.PLANNED) && dto.getLessonStartTime().isBefore(ZonedDateTime.now())) {
            e.rejectValue("lessonStartTime", "lessonStartTime.past", "Cannot create a planned lesson in the past!");
        }

    }
}
