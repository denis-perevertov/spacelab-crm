package com.example.spacelab.validator;

import com.example.spacelab.dto.course.CourseSaveUpdatedDTO;
import com.example.spacelab.dto.lesson.LessonSaveBeforeStartDTO;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.lesson.Lesson;
import com.example.spacelab.repository.AdminRepository;
import com.example.spacelab.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
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



        if(dto.getDate() == null) {
            e.rejectValue("date", "date.empty", "Enter date!");
        }else {
            if (dto.getTime() == null) {
                e.rejectValue("time", "time.empty", "Enter time!");
            } else {
                LocalDateTime dateTime = LocalDateTime.of(dto.getDate(), dto.getTime());
                if (dateTime.isBefore(LocalDateTime.now())) {
                    e.rejectValue("date", "date.before", "Date and time must be in future!");
                }
            }
        }


        if(dto.getCourseId() == null || dto.getCourseId() == 0)
            e.rejectValue("courseID", "courseID.empty", "Select course!");
        else if(!courseRepository.existsById(dto.getCourseId()))
            e.rejectValue("courseID", "courseID.no-match", "Course with this ID doesn't exist!");


        if(dto.getMentorId() == null || dto.getMentorId() == 0)
            e.rejectValue("mentorID", "mentorID.empty", "Select mentor!");
        else if(!adminRepository.existsById(dto.getMentorId()))
            e.rejectValue("mentorID", "mentorID.no-match", "Mentor with this ID doesn't exist!");


        if(dto.getManagerId() == null || dto.getManagerId() == 0)
            e.rejectValue("managerID", "managerID.empty", "Select manager!");
        else if(!adminRepository.existsById(dto.getManagerId()))
            e.rejectValue("managerID", "managerID.no-match", "Manager with this ID doesn't exist!");


        if(dto.getLink() == null || dto.getLink().isEmpty())
            e.rejectValue("link", "link.empty", "Enter link!");
        else if(dto.getLink().length() > 200)
            e.rejectValue("link", "link.length", "Link length: max 200");











    }
}
